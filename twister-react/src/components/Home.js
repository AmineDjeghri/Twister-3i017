import React, { Component } from 'react';
import {Container, Row,Col,Card} from "react-bootstrap";
import '../css/main.css';
import TwistList from "./TwistList";
import DashboardProfileCard from "./DashboardProfileCard";
import axios from "axios";

class Home extends Component {

    constructor(props){

        super(props)



        //examples
        this.comment={username:"mohMidou",
            fullname:"Jean Michel",
            text:'Fadila Boumendjel Chitour, parle des revendication en matière des droits des femmes : « il ne faut se laisser pieger, c’est une priorité (...) le code la famille est un instrument qui légitime la violence » \n',
            likeCounter:50,
            date:'2h'}

        this.commentList=[this.comment,this.comment,this.comment]


        this.twist={
            text:"hello, dadzda dazdadad dadzadadadad adada",
            date:"3h",
            username:"Laka1",
            fullname:"aeert eadad",
            commentList:this.commentList,
            likeCounter: 120,
            commentCounter:23,
        }

        this.twistList=[this.twist,this.twist,this.twist]

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
