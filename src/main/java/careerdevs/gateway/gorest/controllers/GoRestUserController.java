package careerdevs.gateway.gorest.controllers;

import careerdevs.gateway.gorest.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/api/gorest/user")
public class GoRestUserController {

    @Autowired
    private Environment env;

    private static final String GO_REST_URL = "https://gorest.co.in/public/v1/users/";

    @GetMapping("/pageone")
    public GoRestResponseMulti pageOne(RestTemplate restTemplate) {
        return restTemplate.getForObject(GO_REST_URL, GoRestResponseMulti.class);
    }


    @GetMapping("/allusers")
    public Object allUsers(RestTemplate restTemplate) {

        GoRestResponseMulti res = pageOne(restTemplate);
        int pageLimit = res.getMeta().getPagination().getPages();

        ArrayList<GoRestUser> allUsers = new ArrayList<>();
//        Collections.addAll(allUsers, res.getData());

        for (int i = 2; i <= pageLimit; i++) {
            String URL = "https://gorest.co.in/public/v1/users?page=" + i;
            GoRestUser[] userData = restTemplate.getForObject(URL, GoRestResponseMulti.class).getData();
            Collections.addAll(allUsers, userData);
        }
        return allUsers;
    }


    @GetMapping("/someusers")
    public Object someUsers(RestTemplate restTemplate) {

        GoRestResponseMulti res = pageOne(restTemplate);
        int pageLimit = res.getMeta().getPagination().getPages();

        ArrayList<GoRestUser> allUsers = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String URL = "https://gorest.co.in/public/v1/users?page=" + i;
            GoRestUser[] userData = restTemplate.getForObject(URL, GoRestResponseMulti.class).getData();
            Collections.addAll(allUsers, userData);
        }
        return allUsers;
    }


    @GetMapping("/get/{id}")
    public Object getUser(RestTemplate restTemplate, @PathVariable(name = "id") String id) {
        String URL = GO_REST_URL + id;
        try {

            return restTemplate.getForObject(URL, GoRestResponse.class).getData();

        } catch (HttpClientErrorException.NotFound e) {

            return "ID did not match a user in the database";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }


    @GetMapping("/page/{page}")
    public Object getPage(RestTemplate restTemplate, @PathVariable(name = "page") String page) {

        String URL = "https://gorest.co.in/public/v1/users?page=" + page;

        try {
            return restTemplate.getForObject(URL, GoRestResponseMulti.class).getData();
        } catch (Exception e) {
            return "Page does not exist";
        }
    }


    @GetMapping("/test")
    public Object test(@RequestBody Object testObj) {
        return testObj;
    }


    // Post Method v1
    @PostMapping("/post")
    public Object postUser(
            RestTemplate restTemplate,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "gender") String gender,
            @RequestParam(name = "status") String status
    ) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("gorest.token"));

            GoRestUser newUser = new GoRestUser(name, email, gender, status);

            HttpEntity<GoRestUser> request = new HttpEntity(newUser, headers);

            return restTemplate.exchange(GO_REST_URL, HttpMethod.POST, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }



    // Post Method v2
    @PostMapping("/post/v2/{id}")
    public Object postUserv2(
            RestTemplate restTemplate,
            @RequestBody GoRestUser user
    ) {
        try {
            if (!user.getGender().equals("male") && !user.getGender().equals("female")) {
                return "Gender must be entered as male or female";
            }

            if (!user.getStatus().equals("inactive") && !user.getStatus().equals("active")) {
                return "Status must be entered as active or inactive";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("gorest.token"));

            HttpEntity<GoRestUser> request = new HttpEntity(user, headers);

            return restTemplate.exchange(GO_REST_URL, HttpMethod.POST, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
    
    
    
    
    // Put Method v1
    @PutMapping("/put/v2")
    public Object updateUsers1(
            RestTemplate restTemplate,
            @RequestParam(name = "id") String id, // @RequestParam
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "gender", required = false) String gender,
            @RequestParam(name = "status", required = false) String status
            ) {
        String URL = GO_REST_URL + id;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("gorest.token"));

            GoRestUser updateUser = new GoRestUser(name, email, gender, status);

            HttpEntity<GoRestUser> request = new HttpEntity(updateUser, headers);

            return restTemplate.exchange(URL, HttpMethod.PUT, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
    
    
    // Put Method v2
    @PutMapping("/put/v2/{id}")
    public Object updateUsers2(
            RestTemplate restTemplate,
            @PathVariable(name = "id") String id, // @PathVariable
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "gender", required = false) String gender,
            @RequestParam(name = "status", required = false) String status
    ) {
        String URL = GO_REST_URL + id;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("gorest.token"));

            GoRestUser newUser = new GoRestUser(name, email, gender, status);

            HttpEntity<GoRestUser> request = new HttpEntity(newUser, headers);

            return restTemplate.exchange(URL, HttpMethod.PUT, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }


    // Put Method v3
    @PutMapping("/put/v3/{id}")
    public Object updateUsers3(
            RestTemplate restTemplate,
            @PathVariable(name = "id") String id, // @PathVariable
            @RequestBody GoRestUser user
    ) {
        String URL = GO_REST_URL + id;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(env.getProperty("gorest.token"));

            HttpEntity<GoRestUser> request = new HttpEntity(user, headers);

            return restTemplate.exchange(URL, HttpMethod.PUT, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
    
    
    
    // Delete Method v1 (works)
    @DeleteMapping("/delete")
    public String deleteUser(RestTemplate restTemplate, @RequestParam(name = "id") String id) {

        String URL = GO_REST_URL + id;
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

    // Delete Method v2 (works)
    @DeleteMapping("/deletealt/{id}")
    public String deleteUser2(RestTemplate restTemplate, @PathVariable(name = "id") String id) {

        String URL = GO_REST_URL + id;
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
