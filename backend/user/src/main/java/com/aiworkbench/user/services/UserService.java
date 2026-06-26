package com.aiworkbench.user.services;

import com.aiworkbench.user.dtos.UserDTO;
import com.aiworkbench.user.entities.Users;
import com.aiworkbench.user.enums.user_status;
import com.aiworkbench.user.mappers.UserMapper;
import com.aiworkbench.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // 1. Get by ID
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    // 2. Get All
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    // 3 & 4. Get by Email
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDTO);
    }

    // 5. Name and Search
    public List<UserDTO> searchByFullName(String query) {
        // If the query is null or blank, you might want to return an empty list 
        // or all users depending on your business requirements.
        if (query == null || query.isBlank()) {
            return List.of(); 
        }

        // Spring Data JPA's 'Containing' keyword automatically adds the % wildcard
        // to the beginning and end of the string (i.e., %query%).
        return userRepository.findByFullNameContainingIgnoreCase(query)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    // 6. Salary Range Operations
    public List<UserDTO> getBySalaryRange(BigDecimal min, BigDecimal max) {
        return userRepository.findByBaseSalaryBetween(min, max)
                .stream().map(userMapper::toDTO).toList();
    }

    public List<UserDTO> getBySalaryGreaterThan(BigDecimal salary) {
        return userRepository.findByBaseSalaryGreaterThan(salary)
                .stream().map(userMapper::toDTO).toList();
    }

    public List<UserDTO> getBySalaryLessThan(BigDecimal salary) {
        return userRepository.findByBaseSalaryLessThan(salary)
                .stream().map(userMapper::toDTO).toList();
    }

    // 7. Get by Status
    public List<UserDTO> getByStatus(user_status status) {
        return userRepository.findByStatus(status)
                .stream().map(userMapper::toDTO).toList();
    }

    // 8 & 9. Top/Min N Salaries
    public List<UserDTO> getTopPaidUsers(int n) {
        return userRepository.findByOrderByBaseSalaryDesc(PageRequest.of(0, n))
                .stream().map(userMapper::toDTO).toList();
    }

    public List<UserDTO> getMinPaidUsers(int n) {
        return userRepository.findByOrderByBaseSalaryAsc(PageRequest.of(0, n))
                .stream().map(userMapper::toDTO).toList();
    }

    // Write Operations
    @Transactional
    public UserDTO saveUser(UserDTO dto) {
        Users entity = userMapper.toEntity(dto);
        return userMapper.toDTO(userRepository.save(entity));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}