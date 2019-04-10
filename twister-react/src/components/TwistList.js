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


    }

    constructor(props) {
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
        const{twists}=this.state
        return (
            <div>
                <Card >

                    <Card.Body>
                        <Card.Title>Twist Wall</Card.Title>
                        <TwistWrite refreshList={this.getTwists}/>
                    </Card.Body>


                    <ListGroup className="list-group-flush">
                        {twists.map((val) => (
                            <ListGroupItem key={val.id_message}>
                                <Twist refreshList={this.getTwists} twist={val}  />
                            </ListGroupItem>
                        ))}
                    </ListGroup>

                </Card>

            </div>


        );
    }


}

export default TwistList;