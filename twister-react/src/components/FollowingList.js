import React, { Component } from 'react';
import {Button, ListGroup} from 'react-bootstrap';
import '../css/main.css';
import PropTypes from 'prop-types';
import Cookies from "js-cookie";
import axios from "axios";

class FollowingList extends Component {


    constructor(props){

        super(props)
        this.state={
            followingList:[],
        }

    }


    componentDidMount() {
        const params={
            user_id:'4'
        }

        axios.get('http://localhost:8080/Twister/following/list?'+new URLSearchParams(params))
            .then(res => {
                console.log(res);
                console.log(res.data);
                this.setState({followingList:res.data.friends})
            })

        console.log(this.state.followingList)
    }



    render() {
        const {followingList}=this.state

        return (
            <ListGroup >

                {followingList.map((val, index) =>
                    <ListGroup.Item className="d-flex justify-content-around" key={index} >
                            <img
                                width={64}
                                height={64}
                                className="mr-3"
                                src="/img/test.jpg"
                                alt="Generic placeholder"
                            />


                            <a href="#profile" className="text--blue text--bold">{val.login_friend}</a>
                            <a href="#rezfz" className="text--grey text--small">@{val.login_friend}</a>

                    </ListGroup.Item>)

                }

            </ListGroup>

        );
    }
}

export default FollowingList;
