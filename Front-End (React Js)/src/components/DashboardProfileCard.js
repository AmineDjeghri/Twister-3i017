import React, { Component } from 'react';
import {Card, Button, Row, Col, NavDropdown} from "react-bootstrap";
import '../css/main.css';
import Cookies from "js-cookie";
import axios from "axios";
import links from "../utilities/navigationLinks";

class DashboardProfileCard extends Component {

    constructor(props){

        super(props)
        this.state={
            profile:new Object(),
        }
        this.getProfile=this.getProfile.bind(this)

    }

    getProfile (){

        const params={
            key_session:Cookies.get('key_session'),


        }

        axios.get('http://localhost:8080/Twister/user/profile?'+new URLSearchParams(params))
            .then(res => {
                console.log(res);
                console.log(res.data);
                this.setState({profile:res.data})

            })

    }

    componentDidMount() {
            this.getProfile()

    }


    render() {
        const {profile}=this.state

        return (
            <Card className="dashboard-profile">
                <Card.Img className="dashboard-profile__bg " variant="top" src="/img/test.jpg" />
                <Card.Body  className="d-flex align-items-center justify-content-around">
                    <Card.Img className="dashboard-profile__pp "  src="/img/test.jpg" />
                    <div>
                        <a href={links.profile} onClick={Cookies.remove('l_p')}  className="text--blue text--bold">{profile.first_name_user} {profile.family_name_user}</a>
                        <a href={links.profile} onClick={Cookies.remove('l_p')}  className="text--grey text--small"> @{profile.login_user}</a>
                    </div>
                </Card.Body>
                <Card.Footer className="bg-white">

                    <Row>
                        <Col>
                            <div className="d-flex flex-column text--small font-weight-bold">
                                <Card.Link className="text--blue" href="#zada">Twists</Card.Link>
                                <Card.Link className="text--blue" href="#eae">{profile.nb_twists}</Card.Link>
                            </div>
                        </Col>
                        <Col>
                            <div className="d-flex flex-column text--small font-weight-bold">
                                <Card.Link className="text--blue" href="#zada">Abonnés</Card.Link>
                                <Card.Link className="text--blue" href="#eae">{profile.nb_followers}</Card.Link>
                            </div>
                        </Col>
                        <Col>
                            <div className="d-flex flex-column text--small font-weight-bold">
                                <Card.Link className="text--blue" href="#zada">Abonnements</Card.Link>
                                <Card.Link className="text--blue" href="#eae">{profile.nb_following}</Card.Link>
                            </div>
                        </Col>
                    </Row>


                </Card.Footer>
            </Card>
        );
    }
}

export default DashboardProfileCard;
