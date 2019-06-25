import React, { Component } from 'react';
import {Image, Navbar, Nav, Button, Row, Col} from "react-bootstrap";
import '../css/main.css';
import {Card} from "react-bootstrap";
import TwistList from "./TwistList";
import FollowerList from "./FollowerList"
import FollowingList from "./FollowingList"
import Cookies from "js-cookie";
import axios from "axios";
import Home from "./Home";
import links from "../utilities/navigationLinks";

class Profile extends Component {

    constructor(props){

        super(props)

        this.state = {
            //show twists, show followers, show following
            show: "twists",
            profile:new Object(),
            twists:[],
            following:[],
            followers:[],
            login:this.props.login,

        }

        this.showFollowers = this.showFollowers.bind(this)
        this.showFollowing = this.showFollowing.bind(this)
        this.showTwists = this.showTwists.bind(this)
        this.getProfile=this.getProfile.bind(this)
        this.getFollowingList=this.getFollowingList.bind(this)
        this.getFollowersList=this.getFollowersList.bind(this)
        this.getTwists=this.getTwists.bind(this)
        this.isOwner=this.isOwner.bind(this)
        this.follow=this.follow.bind(this)
        this.unfollow=this.unfollow.bind(this)
    }

    getProfile (){

        const params = {
            key_session: Cookies.get('key_session'),
        }

        if(typeof this.state.login !== 'undefined') {

            params.login=this.state.login
        }

        axios.get('http://localhost:8080/Twister/user/profile?'+new URLSearchParams(params))
            .then(res => {
                console.log(res);
                console.log(res.data);
                this.setState({profile:res.data})
                this.getTwists()
            })

    }

    getFollowingList (){

        const params={
            key_session:Cookies.get('key_session'),
            login:this.state.profile.login_user,

        }


        axios.get('http://localhost:8080/Twister/following/list?'+new URLSearchParams(params))
            .then(res => {


                console.log(res);
                console.log(res.data);
                this.setState({following:res.data.following})
            })

    }

    getFollowersList (){

        const params={
            key_session:Cookies.get('key_session'),
            login:this.state.profile.login_user,

        }


        axios.get('http://localhost:8080/Twister/followers/list?'+new URLSearchParams(params))
            .then(res => {


                console.log(res);
                console.log(res.data);
                this.setState({followers:res.data.followers})
            })

    }

    getTwists (){

        const params={
            key_session:Cookies.get('key_session'),
            id_user:this.state.profile.id_user,
        }

        axios.get('http://localhost:8080/Twister/twist/list?'+new URLSearchParams(params))
            .then(res => {
                console.log(res);
                console.log(res.data);
                this.setState({twists:res.data.twists})
            })

    }


    componentDidMount() {
        this.getProfile()
        this.isOwner()
    }




    showFollowers(){
        this.setState(
            {
                show:"followers"
            })
        this.getFollowersList()
    }

    showFollowing(){
        this.setState(
            {
                show:"following"
            })
        this.getFollowingList()
    }

    showTwists(){
        this.setState(
            {
                show:"twists"
            })
    }

    //verify if user is owner of something
    isOwner()
    {
        return (Cookies.get('id_user')===this.state.profile.id_user)
    }


    follow(){
        const params={
            key_session:Cookies.get('key_session'),
            id_friend:this.state.profile.id_user,

        }

        axios.post('http://localhost:8080/Twister/following/follow',null, {params} )
            .then(res => {
                console.log(res);
                console.log(res.data);
                this.getProfile()
            })
    }

    unfollow(){
        const params={
            key_session:Cookies.get('key_session'),
            id_friend:this.state.profile.id_user,

        }

        axios.post('http://localhost:8080/Twister/following/unfollow',null, {params} )
            .then(res => {
                console.log(res);
                console.log(res.data);
                this.getProfile()
            })
    }


    render() {
        const {show,profile,twists,following,followers} =this.state
        return (
            <div>
                <div className="profile__header">
                    <Image className="profile__bg "  src="/img/profile-bg.jpg" />
                    <Navbar bg="light" expand="xs" className="justify-content-around">
                        <Image className="profile__pp" src="/img/test.jpg"/>

                        <Row>
                            <Col>
                                <div className="d-flex flex-column  font-weight-bold">
                                    <Card.Link className="text--grey" href="#zada" onClick={this.showTwists}>Twists</Card.Link>
                                    <Card.Link className="text--blue" href="#eae" onClick={this.showTwists}>{profile.nb_twists}</Card.Link>
                                </div>
                            </Col>

                            <Col>
                                <div className="d-flex flex-column  font-weight-bold">
                                    <Card.Link className="text--grey" href="#zada" onClick={this.showFollowers}>Abonnés</Card.Link>
                                    <Card.Link className="text--blue" href="#eae" onClick={this.showFollowers}>{profile.nb_followers}</Card.Link>
                                </div>
                            </Col>

                            <Col>
                                <div className="d-flex flex-column  font-weight-bold">
                                    <Card.Link className="text--grey" href="#zada" onClick={this.showFollowing}>Abonnements</Card.Link>
                                    <Card.Link className="text--blue" href="#eae" onClick={this.showFollowing}>{profile.nb_following}</Card.Link>
                                </div>
                            </Col>

                        </Row>



                        { this.isOwner() && <Button className="btn--white btn--rounded">Modifier profil</Button> }
                        {!this.isOwner() &&  (
                            profile.isfollowed?
                                <Button className="btn--white btn--rounded" onClick={this.unfollow}>Se désabonner</Button>:
                                <Button className="blue btn--rounded" onClick={this.follow}>S'abonner</Button>
                        ) }
                    </Navbar>
                </div>

                <Row className="main-container main-container__row justify-content-md-center">
                    <Col  md="6">
                        {show==="twists" ? <TwistList showProfile={this.props.showProfile}  refreshList={this.getTwists} twists={twists}/> :(
                            show==="followers" ? <FollowerList followerList={followers}/> :
                                <FollowingList followingList={following}/>
                        )}

                    </Col>
                </Row>

            </div>
        );
    }
}

export default Profile;
