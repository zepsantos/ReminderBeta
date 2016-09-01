package com.example.josep.reminderbeta;

/**
 * Created by zepsantos on 27/08/2016.
 */
import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {


	public String email;

	public User() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	public User(String email) {

		this.email = email;

	}

}
// [END blog_user_class]