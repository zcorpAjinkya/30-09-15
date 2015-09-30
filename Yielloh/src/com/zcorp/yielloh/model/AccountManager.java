
package com.zcorp.yielloh.model;

import android.content.Context;

public class AccountManager extends Session
{
	Session accountManager;

	private String passwordKey = "password";

	private String emailIDKey = "emailID";
	private String accessToken = "accessToken";
	private String refreshToken = "refreshToken";

	private String rememberMeKey = "rememberMe";
	private String loginStatusKey = "loginStatus";
	private String avatarKey = "avatar_link";
	private String coverPhotoKey = "cover_link";

	private String avatarLink = "avatarLinkServer";
	private String coverLink = "coverLinkServer";
	private String userName = "username";
	private String gender = "gender";
	private String date_of_birth = "date_of_birth";
	private String emailServer = "emailServer";

	public AccountManager(Context con)
	{
		accountManager = createSession(con, "AccountManager");
	}

	public void loginUser(String emailID, String password, String accessToken,
			String refreshToken)
	{
		accountManager.putString(emailIDKey, emailID);
		accountManager.putString(passwordKey, password);

		accountManager.putString(this.accessToken, accessToken);
		accountManager.putString(this.refreshToken, refreshToken);

		accountManager.putBoolean(loginStatusKey, true);
	}

	public void userProfile(String avatarLink, String coverLink,
			String usernamme, String gender, String date_of_birth,
			String emailServe)
	{
		accountManager.putString(this.avatarLink, avatarLink);
		accountManager.putString(this.coverLink, coverLink);

		accountManager.putString(this.userName, usernamme);
		accountManager.putString(this.gender, gender);
		accountManager.putString(this.date_of_birth, date_of_birth);
		accountManager.putString(this.emailServer, emailServe);

	}

	public void deleteLoginInfo()
	{

		if (accountManager.getBoolean(loginStatusKey, false))
		{
			accountManager.remove(loginStatusKey);
		}
	}

	public void logoutUser()
	{
		if (accountManager.getBoolean(loginStatusKey, false))
		{
			accountManager.putBoolean(loginStatusKey, false);
		}
	}

	public boolean loginStatus()
	{
		return accountManager.getBoolean(loginStatusKey, false);
	}

	public void rememberMe(String emailID, String password, String accessToken,
			String refreshToken)
	{
		accountManager.putString(emailIDKey, emailID);
		accountManager.putString(passwordKey, password);

		accountManager.putString(this.accessToken, accessToken);
		accountManager.putString(this.refreshToken, refreshToken);

		accountManager.putBoolean(loginStatusKey, true);
		accountManager.putBoolean(rememberMeKey, true);
	}

	public void forgetMe()
	{
		accountManager.putBoolean(rememberMeKey, false);
	}

	public boolean rememberMeStatus()
	{
		return accountManager.getBoolean(rememberMeKey, false);
	}

	public String getPassword()
	{
		return accountManager.getString(passwordKey, null);
	}

	public void setPassword(String password)
	{
		accountManager.putString(passwordKey, password);
	}

	public String getEmailID()
	{
		return accountManager.getString(emailIDKey, null);
	}

	public void setEmailID(String emailId)
	{
		accountManager.putString(emailIDKey, emailId);
	}

	public boolean getLoginStatus()
	{
		return accountManager.getBoolean(loginStatusKey, false);
	}

	public void setAccessToken(String accessToken)
	{
		accountManager.putString(this.accessToken, accessToken);
	}

	public String getAccessToken()
	{
		return accountManager.getString(this.accessToken, null);
	}

	public void setRefreshToken(String refreshToken)
	{
		accountManager.putString(this.refreshToken, refreshToken);
	}

	public String getRefreshToken()
	{
		return accountManager.getString(this.refreshToken, null);
	}

	public void setAvatarLink(String link)
	{
		accountManager.putString(avatarLink, link);
	}

	public String getAvatarLink()
	{
		return accountManager.getString(avatarLink, null);
	}

	public void setCoverPhotoLink(String link)
	{
		accountManager.putString(coverPhotoKey, link);
	}

	public String getCoverPhotoLink()
	{
		return accountManager.getString(coverPhotoKey, null);
	}

	public void setAvatarLinkLogin(String link)
	{
		accountManager.putString(this.avatarLink, link);
	}

	public String getAvatarLinkLogin()
	{
		return accountManager.getString(this.avatarLink, null);
	}

	public void setCoverLinkLogin(String link)
	{
		accountManager.putString(this.coverLink, link);
	}

	public String getCoverLinkLogin()
	{
		return accountManager.getString(this.coverLink, null);
	}

	public void setUserName(String username)
	{
		accountManager.putString(this.userName, username);
	}

	public String getUserName()
	{
		return accountManager.getString(this.userName, null);
	}

	public void setGender(String gender)
	{
		accountManager.putString(this.gender, gender);
	}

	public String getGender()
	{
		return accountManager.getString(this.userName, null);
	}

	public void setDOB(String dob)
	{
		accountManager.putString(this.date_of_birth, dob);
	}

	public String getDOB()
	{
		return accountManager.getString(this.date_of_birth, null);
	}

	public void setEmail(String emailServer)
	{
		accountManager.putString(this.emailServer, emailServer);
	}

	public String getEmail()
	{
		return accountManager.getString(this.emailServer, null);
	}
}
