package com.example.social_network.dtos.Response;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsFeed {
    private List<PostResponse> postResponses;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

}
