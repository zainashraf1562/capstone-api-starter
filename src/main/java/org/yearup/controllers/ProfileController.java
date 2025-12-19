package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Profile getProfileByUserId(Principal principal)
    {
        try
        {
            String principalName = principal.getName();
            User user = userDao.getByUserName(principalName);
            if(user == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found!");
            }
            int userId = user.getId();
            return profileDao.getProfileByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Profile updateProfile (Principal principal, @RequestBody Profile profile){
        try {
            String principalName = principal.getName();
            User user = userDao.getByUserName(principalName);
            if(user == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found!");
            }
            int userId = user.getId();
            profileDao.updateProfile(userId, profile);
            return profile;

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
