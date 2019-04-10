import React, { Component } from 'react';
import '../css/main.css';
import NavigationPannel from './NavigationPannel';
import links from '../utilities/navigationLinks'
import Cookies from 'js-cookie';
import Home from "./Home";
import Profile from "./Profile";
import Signup from "./Signup";
import Login from "./Login";
import Welcome from "./Welcome";
import axios from "axios";

import {BrowserRouter, Route, Link, Redirect} from "react-router-dom";

class App extends Component {
    constructor(props){

        super(props)
        this.state = {
            isLoggedIn:typeof Cookies.get('isLoggedIn')=='undefined'?false :Cookies.get('isLoggedIn'),
            currentPage: "",

            user: typeof Cookies.get('key_session')=='undefined'? new Object() :{
                key_session:Cookies.get('key_session')
            },



        }


        this.setLogout = this.setLogout.bind(this)
        this.getConnected=this.getConnected.bind(this)

    }


    getConnected(user){
        this.setState(
            {
                isLoggedIn:Cookies.get('isLoggedIn'),
                user:{
                    login:Cookies.get('login'),
                    key_session:Cookies.get('key_session'),
                    id_user:Cookies.get('id_user'),
                }

            })

    }

    setLogout(){
        this.setState(
            {
                isLoggedIn:false,
                currentPage:"signup",
                user:new Object(),
            })

    }


    // ajouter après le Route pour profile avec le login comme paramètre
    render() {
        const {isLoggedIn,currentPage,user,twists}=this.state

        return (

            <BrowserRouter>
            <div className="app">
                <NavigationPannel   logout={this.setLogout} isLoggedIn={isLoggedIn} key_session={user.key_session}/>
                {currentPage==="profile" && <Profile/> }


                <div className="main-route">

                    <Route exact path={links.root} render={(props) => isLoggedIn? <Home user={user} /> : <Welcome/>} />
                    <RouteLoggedIn path={links.login} isLoggedIn={isLoggedIn} component={(props) => <Login login={this.getConnected} /> } />
                    <RouteLoggedIn path={links.signIn} isLoggedIn={isLoggedIn} component={(props) => <Signup signup={this.getConnected} /> } />
                    <RouteNotLoggedIn path={links.profile} isLoggedIn={isLoggedIn} component={Profile} />
                </div>

            </div>

            </BrowserRouter>
        );
    }

}

export default App;

//private router and secure redirect
//use this if you want to redirect when the user is logged in
const RouteLoggedIn = ({ component: Component, ...rest }) => (
    <Route {...rest} render={(props) => (
        !rest.isLoggedIn
            ? <Component {...props} />
            : <Redirect to={links.root} />
    )} />
)

//use this if you want to redirect when the user is not logged in
const RouteNotLoggedIn = ({ component: Component, ...rest }) => (
    <Route {...rest} render={(props) => (
        rest.isLoggedIn
            ? <Component {...props} />
            : <Redirect to={links.login} />
    )} />
)