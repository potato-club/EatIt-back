package com.gamza.ItEat.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi AllApi() {
        Info info = new Info().title("모든 API").version("v0.1");

        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .displayName("All API")
                .addOpenApiCustomizer(api -> api.setInfo(info))
                .build();
    }

    @Bean
    public GroupedOpenApi postApi() {
        Info info = new Info().title("게시물 관련 API").version("v0.1");
        String[] paths = {"/post/**", "/search"};

        return GroupedOpenApi.builder()
                .group("post")
                .pathsToMatch(paths)
                .displayName("Post's API")
                .addOpenApiCustomizer(api -> api.setInfo(info))
                .build();
    }

    @Bean
    public GroupedOpenApi likeApi() {
        Info info = new Info().title("좋아요 관련 API").version("v0.1");
        String[] paths = {"/like/**"};

        return GroupedOpenApi.builder()
                .group("like")
                .pathsToMatch(paths)
                .displayName("Like's API")
                .addOpenApiCustomizer(api -> api.setInfo(info))
                .build();
    }

    @Bean
    public GroupedOpenApi commentApi() {
        Info info = new Info().title("댓글 관련 API").version("v0.1");
        String[] paths = {"/comment/**"};

        return GroupedOpenApi.builder()
                .group("comment")
                .pathsToMatch(paths)
                .displayName("Comment's API")
                .addOpenApiCustomizer(api -> api.setInfo(info))
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        Info info = new Info().title("유저 관련 API").version("v0.1");
        String[] paths = {"/client/**"};

        return GroupedOpenApi.builder()
                .group("client")
                .pathsToMatch(paths)
                .displayName("Client's API")
                .addOpenApiCustomizer(api -> api.setInfo(info))
                .build();
    }
}
