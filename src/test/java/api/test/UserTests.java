package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	Faker faker;
	User userPayload;
	public Logger logger;
	@BeforeClass
	public void setupData()
	{
		faker= new Faker();
		userPayload= new User();
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		userPayload.setPassword(faker.internet().password());
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//Logs
		logger=LogManager.getLogger(this.getClass());
	}
	@Test(priority=1)
	public void testPostUser()
	{
		logger.info("************* Create User ***********");
		Response response=UserEndPoints.createUser(userPayload);
		response.then().log().body();
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("************** User is Created ***********");
	}
	@Test(priority=2)
	public void testGetUserByName()
	{
		logger.info("*********** Reading User Info ************");
		Response response=UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().body();
		//response.statusCode();
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("Details of Created User ***********");
	}
	
	@Test(priority=3)
	public void updateUserByName()
	{
		
		//Update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		
		logger.info("********** Updating User *************");
		Response response=UserEndPoints.updateUser(userPayload,this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("********** User Updated *************");
		//Checking data after update
		Response responseAfterUpdate=UserEndPoints.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(),200);
	}
	@Test(priority=4)	
	public void testDeleteUserByName()
	{
		logger.info("*********** Deleting User *********");
		Response response=UserEndPoints.deleteUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("********** User Deleted **********");
	}
}
