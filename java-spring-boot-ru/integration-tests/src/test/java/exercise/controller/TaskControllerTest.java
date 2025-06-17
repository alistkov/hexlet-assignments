package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testCreate() throws Exception {
        var task = generateAndSaveTask();

        var request = post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(task));

        mockMvc.perform(request)
            .andExpect(status().isCreated());

        task = taskRepository.findById(task.getId()).get();

        assertThat(task.getTitle()).isEqualTo("Test task title");
        assertThat(task.getDescription()).isEqualTo("Test task description");
    }

    @Test
    public void testUpdate() throws Exception {
        var task = generateAndSaveTask();

        var data = new HashMap<>();
        data.put("title", "Updated task title");
        data.put("description", "Updated description");

        var request = put("/tasks/" + task.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));

        mockMvc.perform(request)
            .andExpect(status().isOk());

        task = taskRepository.findById(task.getId()).get();

        assertThat(task.getTitle()).isEqualTo("Updated task title");
        assertThat(task.getDescription()).isEqualTo("Updated description");
    }

    @Test
    public void testDelete() throws Exception {
        var task = generateAndSaveTask();

        var request = delete("/tasks/" + task.getId());

        mockMvc.perform(request)
            .andExpect(status().isOk());

        var tryFindDeletedTask = taskRepository.findById(task.getId());

        assertThat(tryFindDeletedTask).isEmpty();
    }

    private Task generateAndSaveTask() {
        var task = Instancio.of(Task.class)
            .ignore(Select.field(Task::getId))
            .supply(Select.field(Task::getTitle), () -> "Test task title")
            .supply(Select.field(Task::getDescription), () -> "Test task description")
            .create();

        taskRepository.save(task);

        return task;
    }
    // END
}
