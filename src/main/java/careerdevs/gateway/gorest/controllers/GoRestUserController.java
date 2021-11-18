package careerdevs.gateway.gorest.controllers;

import careerdevs.gateway.gorest.models.GoRestResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/gorest/user")
public class GoRestUserController {

    @GetMapping("/pageone")
    public Object allUser(RestTemplate restTemplate) {
        return restTemplate.getForObject("https://gorest.co.in/public/v1/users/", GoRestResponse.class).getData();
    }

    @GetMapping("/get")
    public Object getUser(RestTemplate restTemplate, @RequestParam(name = "id") String id) {
        String URL = "https://gorest.co.in/public/v1/users/" + id;
        return restTemplate.getForObject(URL, GoRestResponse.class).getData();
    }

    @DeleteMapping("/delete")
    public Object deleteUser(RestTemplate restTemplate, @RequestParam(name = "id") String id) {
        String URL = "https://gorest.co.in/public/v1/users/" + id;
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer 14df59a45c1848d03013a93a010853a9e632fd8d4cf773b6aa8053e894f34ce2");

            HttpEntity request = new HttpEntity(headers);

            restTemplate.delete(URL, request);
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
