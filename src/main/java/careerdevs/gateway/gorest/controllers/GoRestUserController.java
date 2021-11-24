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

@RestController
@RequestMapping("/api/gorest/user")
public class GoRestUserController {

    @Autowired
    private Environment env;

    @GetMapping("/pageone")
    public Object pageOne(RestTemplate restTemplate) {
        return restTemplate.getForObject("https://gorest.co.in/public/v1/users/", Object.class);
    }


//    @GetMapping("/allusers")
//    public Object allUsers(RestTemplate restTemplate) {
//
//        GoRestResponseMulti res = pageOne(restTemplate);
//        int pageLimit = res.getMeta().getPagination().getPages();
//
//        ArrayList<GoRestUser> allUsers = new ArrayList<>();
//        Collections.addAll(allUsers, res.getData());
//
//        for (int i = 2; i <= pageLimit; i++) {
//
//        }
//
//        return "null";
//    }


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


    @GetMapping("/page/{page}")
    public Object getPage(RestTemplate restTemplate, @PathVariable(name = "page") String page) {
        String URL = "https://gorest.co.in/public/v1/users?page=" + page;

        try {

            return restTemplate.getForObject(URL, Object.class);

        } catch (Exception e) {

            return "Page does not exist";
        }
    }


    // Post Method V2
    @PostMapping("/post/v2")
    public Object postUserv2(
            RestTemplate restTemplate,
            @RequestBody GoRestUser user
    ) {
        String URL = "https://gorest.co.in/public/v1/users/";
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

            return restTemplate.exchange(URL, HttpMethod.POST, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }


    @GetMapping("/test")
    public Object test(@RequestBody Object testObj) {
        return testObj;
    }


    // Post Method V1
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

            GoRestUser newUser = new GoRestUser(name, email, gender, status);

            HttpEntity<GoRestUser> request = new HttpEntity(newUser, headers);

            return restTemplate.exchange(URL, HttpMethod.POST, request, GoRestResponse.class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }


    // Put Method v1
    @PutMapping("/put/v1")
    public Object updateUsers1(
            RestTemplate restTemplate,
            @RequestParam(name = "id") String id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "gender") String gender,
            @RequestParam(name = "status") String status
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
            @PathVariable(name = "id") String id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "gender") String gender,
            @RequestParam(name = "status") String status
    ) {
        String URL = "https://gorest.co.in/public/v1/users/" + id;
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
