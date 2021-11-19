package careerdevs.gateway.gorest.controllers;

import careerdevs.gateway.gorest.models.GoRestResponse;
import careerdevs.gateway.gorest.models.GoRestUsers;
import careerdevs.gateway.gorest.models.UpdatedUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/gorest/user")
public class GoRestUserController {

    @Autowired
    Environment env;

    @GetMapping("/pageone")
    public Object allUser(RestTemplate restTemplate) {
        return restTemplate.getForObject("https://gorest.co.in/public/v1/users/", GoRestResponse.class).getData();
    }

    @GetMapping("/get")
    public Object getUser(RestTemplate restTemplate, @RequestParam(name = "id") String id) {
        String URL = "https://gorest.co.in/public/v1/users/" + id;
        try {

            return restTemplate.getForObject(URL, GoRestResponse.class).getData();

        } catch (HttpClientErrorException.NotFound e) {

            return "ID did not match a user in the database";

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @PostMapping("/post")
    public Object postUser(
            RestTemplate restTemplate,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "gender") String gender,
            @RequestParam(name = "status") String status
    ) {
        String URL = "https://gorest.co.in/public/v1/users/";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("gorest.token"));

            GoRestUsers newUser = new GoRestUsers(name, email, gender, status);

            HttpEntity<GoRestUsers> request = new HttpEntity(newUser, headers);

            return restTemplate.exchange(URL, HttpMethod.POST, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

//    @PutMapping("/put/{id}")
//    public ResponseEntity<GoRestUsers> updateUser(
//
//    }

    @PutMapping("/put")
    public Object updateUsers(
            RestTemplate restTemplate,
            @RequestParam(name = "id") String id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam (name = "gender") String gender,
            @RequestParam (name = "status") String status
    ) {
        String URL = "https://gorest.co.in/public/v1/users/" + id;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("gorest.token"));

            UpdatedUsers updateUser = new UpdatedUsers();
            updateUser.setName(name);
            updateUser.setEmail(email);
            updateUser.setGender(gender);
            updateUser.setStatus(status);

            HttpEntity<GoRestUsers> request = new HttpEntity(updateUser, headers);

            return restTemplate.exchange(URL, HttpMethod.PUT, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public Object deleteUser(RestTemplate restTemplate, @RequestParam(name = "id") String id) {

        String URL = "https://gorest.co.in/public/v1/users/" + id;
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("gorest.token"));

            HttpEntity request = new HttpEntity(headers);

            restTemplate.exchange(URL, HttpMethod.DELETE, request, GoRestResponse.class);

            return "Successfully deleted user: " + id;

        } catch (HttpClientErrorException.Unauthorized e) {

            return "You need to have authorization";

        } catch (HttpClientErrorException.NotFound e) {

            return "ID did not match a user in the database";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }


//    @GetMapping("/test")
//    public String testRoute() {
//        return "This test did not work!!";
//    }
//
//    @PostMapping("/test")
//    public String testPostRoute() {
//        return "TEST!!!";
//    }
//
//    @PutMapping("/test")
//    public String testPostRoute2() {
//        return "TEST!!!";
//    }
//
//    @DeleteMapping("/test")
//    public String testPostRoute3() {
//        return "TEST!!!";
//    }

//    @RequestMapping("/secondtestroute")
//    public String test() {
//        return "TESTING 123";
//    }
}
