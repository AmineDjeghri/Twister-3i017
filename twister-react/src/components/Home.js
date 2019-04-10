import React, { Component } from 'react';
import {Container, Row,Col,Card} from "react-bootstrap";
import '../css/main.css';
import TwistList from "./TwistList";
import DashboardProfileCard from "./DashboardProfileCard";
import axios from "axios";
import Cookies from "js-cookie";

class Home extends Component {

    constructor(props){

        super(props)

    }



    render() {



        return (
            <div className="main-container">
                    <Row className="main-container__row">

                        <Col  md={3}>

                                <DashboardProfileCard />
                        </Col>

                        <Col  md={6} >
                            <TwistList />
                        </Col>


                        <Col  md={3} >
                            <Card>
                                suggestions de followers
                            </Card>
                        </Col>

                    </Row>






            </div>
        );
    }
}

export default Home;
