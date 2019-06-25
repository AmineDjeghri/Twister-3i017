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
import {BrowserRouter, Route, Link, Redirect} from "react-router-dom";
import SearchUser from "./SearchUser";

class App extends Component {
    constructor(props){

        super(props)
        this.state = {
            isLoggedIn:typeof Cookies.get('isLoggedIn')=='undefined'?false :Cookies.get('isLoggedIn'),
            currentPage: "",

            user: typeof Cookies.get('key_session')=='undefined'? new Object() :{
                key_session:Cookies.get('key_session')
            },

            searchList:[],
            login:undefined
        }
        this.setLogout = this.setLogout.bind(this)
        this.getConnected=this.getConnected.bind(this)
        this.search=this.search.bind(this)
        this.showProfile=this.showProfile.bind(this)
        console.log('app.js constructor called')

    }


    getConnected(user){
        this.setState(
            {
                //pour l'affichage conditionnel des composants de quelques boutons
                isLoggedIn:Cookies.get('isLoggedIn'),
                user:{
                    //pour la verification et l'affichage conditionnel de supprimer , ajouter, ect..
                    id_user:Cookies.get('id_user'),
                    key_session:Cookies.get('key_session'),
                }

            })
        console.log(this.state.id_user)

    }


    setLogout(){
        this.setState(
            {
                isLoggedIn:false,
                user:new Object(),
            })

    }

    search(searchList){

        this.setState(
            {
                searchList:searchList
            })

    }

    showProfile(login){
            this.setState({
                login:login
            })
        console.log('app.js show profile called '+this.state.login)
    }

    // ajouter après le Route pour profile avec le login comme paramètre
    render() {
        const {isLoggedIn,user,searchList,login}=this.state

        return (

            <BrowserRouter>
            <div className="app">
                <NavigationPannel search={this.search}  logout={this.setLogout} isLoggedIn={isLoggedIn} key_session={user.key_session}/>



                <div className="main-route">

                    <Route exact path={links.root} render={(props) => isLoggedIn? <Home showProfile={this.showProfile}/> : <Welcome/>} />
                    <RouteLoggedIn path={links.login} isLoggedIn={isLoggedIn} component={(props) => <Login login={this.getConnected} /> } />
                    <RouteLoggedIn path={links.signIn} isLoggedIn={isLoggedIn} component={(props) => <Signup signup={this.getConnected} /> } />
                    <RouteLoggedIn path={links.signIn} isLoggedIn={isLoggedIn} component={(props) => <Signup signup={this.getConnected} /> } />
                    <RouteNotLoggedIn path={links.profile} isLoggedIn={isLoggedIn} component={(props) => <Profile showProfile={this.showProfile} login={login}/> }  />
                    <RouteNotLoggedIn path={links.search} isLoggedIn={isLoggedIn} component={(props) => <SearchUser showProfile={this.showProfile} searchList={searchList}/> } />
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