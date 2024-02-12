package app.mybad.domain.models

import net.openid.appauth.ResponseTypeValues

object AuthConfig {
    const val AUTHORIZE_URI = "https://vitatracker-heroku.herokuapp.com/oauth2/authorization/google"
    const val LOGOUT_URI = "https://vitatracker-heroku.herokuapp.com/oauth2/logout/google"

    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val APPROVAL_PROMPT = "force"
    //    const val APPROVAL_PROMPT = "auto"
    const val SCOPE = "email profile"

    const val TOKEN_URI = "https://vitatracker-heroku.herokuapp.com/oauth2/token"

    // YOUR CLIENT ID
    const val CLIENT_ID = "447480675069-vis7jr0p9qpsc18lf5ttgrp54uo5c6l7.apps.googleusercontent.com"

    // YOUR CLIENT SECRET
    const val CLIENT_SECRET = ""
    const val CALLBACK_URL = "https://localhost/com.herokuapp.vitatracker-heroku"
//    const val CALLBACK_URL = "https://vitatracker-heroku.herokuapp.com"
//    const val CALLBACK_URL = "https://vitatracker-heroku.herokuapp.com/login/oauth2/code/google"
}
