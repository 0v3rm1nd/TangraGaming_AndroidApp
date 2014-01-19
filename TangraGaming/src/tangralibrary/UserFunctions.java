package tangralibrary;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class UserFunctions {

	private JSONParser jsonParser;

	// Testing in localhost using wamp or xampp
	// connect to server
	private static String loginURL = "http://0v3rm1nd.com/TangraAPI/";
	private static String registerURL = "http://0v3rm1nd.com/TangraAPI/";
	private static String getUserStatisticalDataURL = "http://0v3rm1nd.com/TangraAPI/";
	private static String tangraApiURL = "http://0v3rm1nd.com/TangraAPI/";

	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String getUserStatisticalData_tag = "getUserStatisticalData";
	private static String upadteUserData_tag = "updateUserData";
	private static String resetpassword_tag = "resetpassword";
	private static String getMainRoomData_tag = "getMainRooms";
	private static String getRoomMembership_tag = "getRoomMembership";
	private static String createRoom_tag = "createroom";
	private static String getSubRooms_tag = "getSubRooms";
	private static String joinRoom_tag = "joinRoom";
	private static String leaveRoom_tag = "leaveRoom";
	private static String searchRoom_tag = "searchRooms";
	private static String searchFriend_tag = "searchFriend";
	private static String inviteFriend_tag = "inviteFriend";
	private static String getFriends_tag = "getFriends";
	private static String removeFriend_tag = "removeFriend";
	private static String getFriendRequests_tag = "getFriendRequests";
	private static String acceptFriendRequest_tag = "acceptFriendRequest";
	private static String declineFriendRequest_tag = "declineFriendRequest";
	private static String getFriendRequstStatuses_tag = "getFriendRequestStatuses";
	private static String getPosts_tag = "getPosts";
	private static String makePost_tag = "makePost";

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param nickname
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function make Regsiter Request
	 * 
	 * @param email
	 * @param nickname
	 * @param password
	 * */
	public JSONObject registerUser(String email, String nickname,
			String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("nickname", nickname));
		params.add(new BasicNameValuePair("password", password));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}

	/**
	 * function make total posts request
	 * 
	 * @param email
	 * @param name
	 * @param location
	 * @param gender
	 * @param birthday
	 * @param hobby
	 * */
	public JSONObject updateUserData(String email, String name,
			String location, String gender, String birthday, String hobby) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", upadteUserData_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("gender", gender));
		params.add(new BasicNameValuePair("birthday", birthday));
		params.add(new BasicNameValuePair("hobby", hobby));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to update user data @param email
	 * */
	public JSONObject getUserStatisticalData(String email) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getUserStatisticalData_tag));
		params.add(new BasicNameValuePair("email", email));
		JSONObject json = jsonParser.getJSONFromUrl(getUserStatisticalDataURL,
				params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to reset password
	 * 
	 * @param email
	 * @param oldpassword
	 * @param newpassword
	 * */
	public JSONObject resetPassword(String email, String oldpassword,
			String newpassword) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", resetpassword_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("oldpassword", oldpassword));
		params.add(new BasicNameValuePair("newpassword", newpassword));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to get main room data NO @param
	 * */
	public JSONObject getMainRoomData() {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getMainRoomData_tag));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to get room membership
	 * 
	 * @param email
	 * */
	public JSONObject getRoomMembership(String email) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getRoomMembership_tag));
		params.add(new BasicNameValuePair("email", email));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to create new room
	 * 
	 * @param name
	 * @param owner
	 * @param parentroom
	 * */
	public JSONObject createRoom(String name, String owner, String parentroom) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", createRoom_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("owner", owner));
		params.add(new BasicNameValuePair("parentroom", parentroom));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to get sub rooms for each main room
	 * 
	 * @param parentroom
	 * */
	public JSONObject getSubRooms(String parentroom) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getSubRooms_tag));
		params.add(new BasicNameValuePair("parentroom", parentroom));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to join a room
	 * 
	 * @param user
	 * @param room
	 * */
	public JSONObject joinRoom(String user, String room) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", joinRoom_tag));
		params.add(new BasicNameValuePair("email", user));
		params.add(new BasicNameValuePair("roomname", room));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to leave a room
	 * 
	 * @param user
	 * @param room
	 * */
	public JSONObject leaveRoom(String user, String room) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", leaveRoom_tag));
		params.add(new BasicNameValuePair("email", user));
		params.add(new BasicNameValuePair("roomname", room));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to get a list of rooms based on a user search
	 * 
	 * @param room
	 * */
	public JSONObject searchRoom(String room) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", searchRoom_tag));
		params.add(new BasicNameValuePair("roomname", room));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to get a list of users based on a user search
	 * 
	 * @param usernickname
	 * */
	public JSONObject searchUser(String useremail) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", searchFriend_tag));
		params.add(new BasicNameValuePair("useremail", useremail));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to make a friend request
	 * 
	 * @param from
	 * @param to
	 * */
	public JSONObject inviteFriend(String from, String to) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", inviteFriend_tag));
		params.add(new BasicNameValuePair("from", from));
		params.add(new BasicNameValuePair("to", to));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to get a list of friends
	 * 
	 * @param user1
	 * */
	public JSONObject getFriends(String user) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getFriends_tag));
		params.add(new BasicNameValuePair("user1", user));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to remvoe friend
	 * 
	 * @param user1
	 * @param user2
	 * */
	public JSONObject removeFriend(String user1, String user2) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", removeFriend_tag));
		params.add(new BasicNameValuePair("user1", user1));
		params.add(new BasicNameValuePair("user2", user2));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to get friend requests
	 * 
	 * @param to
	 * */
	public JSONObject getFriendRequests(String to) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getFriendRequests_tag));
		params.add(new BasicNameValuePair("to", to));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to accept friend request
	 * 
	 * @param from
	 * @param to
	 * 
	 * */
	public JSONObject acceptFriendRequest(String from, String to) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", acceptFriendRequest_tag));
		params.add(new BasicNameValuePair("from", from));
		params.add(new BasicNameValuePair("to", to));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to decline friend request
	 * 
	 * @param from
	 * @param to
	 * 
	 * */
	public JSONObject declineFriendRequest(String from, String to) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", declineFriendRequest_tag));
		params.add(new BasicNameValuePair("from", from));
		params.add(new BasicNameValuePair("to", to));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function to get your friend request statuses
	 * 
	 * @param from
	 * 
	 * */
	public JSONObject getFriendRequestStatuses(String from) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getFriendRequstStatuses_tag));
		params.add(new BasicNameValuePair("from", from));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function to get the posts of a specific room in the forum
	 * 
	 * @param roomname
	 * 
	 * */
	public JSONObject getPosts(String roomname) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getPosts_tag));
		params.add(new BasicNameValuePair("roomname", roomname));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function to submit post
	 * 
	 * @param user
	 * @param room
	 * @param post
	 * 
	 * */
	public JSONObject makePosts(String user, String room, String post) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", makePost_tag));
		params.add(new BasicNameValuePair("user", user));
		params.add(new BasicNameValuePair("room", room));
		params.add(new BasicNameValuePair("post", post));
		JSONObject json = jsonParser.getJSONFromUrl(tangraApiURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if (count > 0) {
			// user logged in
			return true;
		}
		return false;
	}

	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}

}