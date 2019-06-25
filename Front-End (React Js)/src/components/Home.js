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
        this.state={
            twists:[],
        }
        this.getTwists=this.getTwists.bind(this)
    }




    getTwists (){

        const params={
            key_session:Cookies.get('key_session'),
        }

        axios.get('http://localhost:8080/Twister/twist/wall?'+new URLSearchParams(params))
            .then(res => {
                console.log(res);
                console.log(res.data);
                this.setState({twists:res.data.twists})
            })

    }

    componentDidMount() {
        this.getTwists()
    }

    render() {

            const {twists}=this.state

        return (
            <div className="main-container">
                    <Row className="main-container__row">

                        <Col  md={3}>

                                <DashboardProfileCard />
                        </Col>

                        <Col  md={6} >
                            <TwistList showProfile={this.props.showProfile} refreshList={this.getTwists} twists={twists}/>
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
