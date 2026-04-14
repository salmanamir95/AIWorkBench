package com.aiworkbench.project.intelligence.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aiworkbench.project.gr.GR;
import com.aiworkbench.project.intelligence.client.dto.ProjectActivityLogDTO;
import com.aiworkbench.project.intelligence.client.dto.ProjectDTO;
import com.aiworkbench.project.intelligence.client.dto.ProjectMemberDTO;
import com.aiworkbench.project.intelligence.client.dto.ProjectSettingsDTO;
import com.aiworkbench.project.intelligence.client.dto.PageResponse;

@Component
public class ProjectServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProjectServiceClient(RestTemplate restTemplate,
                                @Value("${services.project.base-url:http://localhost:8080}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ProjectDTO getProject(Long projectId) {
        String url = baseUrl + "/api/projects/" + projectId;
        ResponseEntity<GR<ProjectDTO>> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return resp.getBody() != null ? resp.getBody().getData() : null;
    }

    public ProjectSettingsDTO getProjectSettings(Long projectId) {
        String url = baseUrl + "/api/projects/" + projectId + "/settings";
        ResponseEntity<GR<ProjectSettingsDTO>> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return resp.getBody() != null ? resp.getBody().getData() : null;
    }

    public List<ProjectMemberDTO> getProjectMembers(Long projectId, int size) {
        String url = baseUrl + "/api/projects/" + projectId + "/members?page=0&size=" + size;
        ResponseEntity<GR<PageResponse<ProjectMemberDTO>>> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        PageResponse<ProjectMemberDTO> page = resp.getBody() != null ? resp.getBody().getData() : null;
        return page != null ? page.getContent() : List.of();
    }

    public List<ProjectActivityLogDTO> getProjectActivity(Long projectId, int size) {
        String url = baseUrl + "/api/projects/" + projectId + "/activity?page=0&size=" + size;
        ResponseEntity<GR<PageResponse<ProjectActivityLogDTO>>> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        PageResponse<ProjectActivityLogDTO> page = resp.getBody() != null ? resp.getBody().getData() : null;
        return page != null ? page.getContent() : List.of();
    }

    public List<ProjectDTO> getProjectsByUser(Long userId, int size) {
        String url = baseUrl + "/api/projects/user/" + userId + "?page=0&size=" + size;
        ResponseEntity<GR<PageResponse<ProjectDTO>>> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        PageResponse<ProjectDTO> page = resp.getBody() != null ? resp.getBody().getData() : null;
        return page != null ? page.getContent() : List.of();
    }

    public List<ProjectDTO> getAllProjects(int page, int size) {
        String url = baseUrl + "/api/projects?page=" + page + "&size=" + size;
        ResponseEntity<GR<PageResponse<ProjectDTO>>> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        PageResponse<ProjectDTO> pageResp = resp.getBody() != null ? resp.getBody().getData() : null;
        return pageResp != null ? pageResp.getContent() : List.of();
    }
}
