The ThousandEyes Challenge

Please find below the set of REST API Endpoints.
All the endpoints require Basic HTTP Authentication. Therefore, the following credentials are requried:
Header Name: Authorization
Username: The current user ID from the Person Table
Password: The current user Name from the Person Table

Additional Headers are mentioned below as needed.

Feature 1:
http://localhost:8080/get-tweets
Parameter Name: search
Value: The search keyword to filter the messages

Feature 2:
http://localhost:8080/get-following
http://localhost:8080/get-followers

Feature 3:
http://localhost:8080/follow-user
Parameter Name: person_id
Value: The ID of the person you want to follow

Feature 4:
http://localhost:8080/unfollow-user

Parameter Name: person_id
Value: The ID of the person you want to unfollow

Extra Feature 1:
http://localhost:8080/shortest-distance

Parameter Name: person_id
Value: The ID of the target person for which you want to find the distance
