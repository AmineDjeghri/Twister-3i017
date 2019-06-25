import React, { Component } from 'react';
import {Button, ListGroup} from 'react-bootstrap';
import '../css/main.css';
import PropTypes from 'prop-types';
import Cookies from "js-cookie";
import axios from "axios";
import links from "../utilities/navigationLinks";

class FollowingList extends Component {


    constructor(props){

        super(props)

    }


    render() {
        const {followingList}=this.props

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
                            <a href="" className="text--blue text--bold">{val.firstname_following} {val.familyname_following}</a>
                            <a href="#rezfz" className="text--grey text--small">@{val.login_following}</a>

                    </ListGroup.Item>)

                }

            </ListGroup>

        );
    }
}

export default FollowingList;
