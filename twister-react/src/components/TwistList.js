import React, { Component } from 'react';
import {Card,ListGroup,ListGroupItem} from "react-bootstrap";
import '../css/main.css';
import PropTypes from 'prop-types';
import Comment from "./Comment";
import Twist from "./Twist";
import TwistWrite from "./TwistWrite";
import axios from "axios";
import Cookies from 'js-cookie';



class TwistList extends Component {

    static propTypes={
        twists:PropTypes.arrayOf(PropTypes.object),

    }

    constructor(props) {
        super(props)

        this.state={
            twists:[],
        }

        this.getWallTwists=this.getWallTwists.bind(this)

    }


    getWallTwists(){

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


    render() {
        const{twists}=this.state
        return (
            <div>
                    <button onClick={this.getWallTwists}> click</button>
                <Card >

                    <Card.Body>
                        <Card.Title>Twist Wall</Card.Title>
                        <TwistWrite updateWall={this.getWallTwists}/>
                    </Card.Body>


                    <ListGroup className="list-group-flush">
                        {twists.map((val, index) => (
                            <ListGroupItem key={index}>
                                <Twist twist={val}  />
                            </ListGroupItem>
                        ))}
                    </ListGroup>

                </Card>

            </div>


        );
    }


}

export default TwistList;