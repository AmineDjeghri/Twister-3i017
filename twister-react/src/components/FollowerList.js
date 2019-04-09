import React, { Component } from 'react';
import {ListGroup, Button, Card, Media} from 'react-bootstrap';
import '../css/main.css';
import PropTypes from 'prop-types';
import Comment from "./CommentList";

class FollowerList extends Component {


    constructor(props){

        super(props)

        this.testList=['a','b','c']
    }


    render() {

        const{followerList}=this.props

        return (



            <ListGroup >
                {followerList.map((val, index) =>
                    <ListGroup.Item className="d-flex justify-content-around" key={index} >
                        <div>
                            <img
                                width={64}
                                height={64}
                                className="mr-3"
                                src="/img/test.jpg"
                                alt="Generic placeholder"
                            />


                            <a href="#profile" className="text--blue text--bold">{val.fullname}</a>
                            <a href="#" className="text--grey text--small">@{val.username}</a>
                        </div>
                        <div className="">

                            <Button className="btn--blue " size="sm" >S'abonner </Button>
                        </div>

                    </ListGroup.Item>)

                }

            </ListGroup>
        );
    }
}

export default FollowerList;
