package aiworkbench.auth.user.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiworkbench.auth.gr.gR;
import com.aiworkbench.auth.user.entity.User;
import com.aiworkbench.auth.user.service.UserService;

import lombok.RequiredArgsConstructor;
import main.java.com.aiworkbench.auth.gr.gR;
import main.java.com.aiworkbench.auth.user.entity.User;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    
    private final UserService userService;

    // ── AUTH ──────────────────────────────────────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<gR<User>> register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        try {
            return ResponseEntity.ok(gR.success(userService.register(username, email, password), "User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @PostMapping("/login/username")
    public ResponseEntity<gR<User>> loginByUsername(
            @RequestParam String username,
            @RequestParam String password) {
        try {
            return ResponseEntity.ok(gR.success(userService.loginByUsername(username, password), "Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @PostMapping("/login/email")
    public ResponseEntity<gR<User>> loginByEmail(
            @RequestParam String email,
            @RequestParam String password) {
        try {
            return ResponseEntity.ok(gR.success(userService.loginByEmail(email, password), "Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<gR<User>> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(gR.success(userService.findById(id), "User fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<gR<Page<User>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(gR.success(userService.getAll(pageable), "Users fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<gR<User>> update(
            @PathVariable Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password) {
        try {
            return ResponseEntity.ok(gR.success(userService.update(id, username, email, password), "User updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<gR<Void>> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok(gR.success(null, "User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    // ── VERIFIED ──────────────────────────────────────────────────────────────

    @GetMapping("/verified")
    public ResponseEntity<gR<Page<User>>> getAllVerified(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(gR.success(userService.getAllVerifiedTrue(pageable), "Verified users fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @GetMapping("/unverified")
    public ResponseEntity<gR<Page<User>>> getAllUnverified(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(gR.success(userService.getAllVerifiedFalse(pageable), "Unverified users fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    // ── EMAILS & USERNAMES ────────────────────────────────────────────────────

    @GetMapping("/emails")
    public ResponseEntity<gR<Page<String>>> getAllEmails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(gR.success(userService.getAllEmails(pageable), "Emails fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @GetMapping("/usernames")
    public ResponseEntity<gR<Page<String>>> getAllUsernames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(gR.success(userService.getAllUsername(pageable), "Usernames fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    // ── TIME RANGE ────────────────────────────────────────────────────────────

    @GetMapping("/before")
    public ResponseEntity<gR<Page<User>>> getAllBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(gR.success(userService.getAllAccountsBefore(time, pageable), "Users fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @GetMapping("/after")
    public ResponseEntity<gR<Page<User>>> getAllAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(gR.success(userService.getAllAccountsAfter(time, pageable), "Users fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

    @GetMapping("/range")
    public ResponseEntity<gR<Page<User>>> getAllInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(gR.success(userService.getAllAccountsInTimeRange(start, end, pageable), "Users fetched successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(gR.failure(e.getMessage()));
        }
    }

}
