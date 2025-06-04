package exercise;

import java.net.URI;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;
import lombok.Setter;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    @Setter
    private static  List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> index(
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "1") int page
    ) {
        var responseBody = posts.stream()
            .skip((long) (page - 1) * limit)
            .limit(limit)
            .toList();

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(posts.size()))
            .body(responseBody);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> show(@PathVariable String id) {
        var post = posts.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst();

        return ResponseEntity.of(post);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post post) {
        posts.add(post);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
        var existingPost = posts.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst();

        if (existingPost.isPresent()) {
            var post = existingPost.get();
            post.setId(data.getId());
            post.setBody(data.getBody());
            post.setTitle(data.getTitle());
            return ResponseEntity.ok()
                .body(post);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .build();

    }
    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
