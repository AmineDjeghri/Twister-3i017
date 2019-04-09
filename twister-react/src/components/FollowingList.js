import React, { Component } from 'react';
import {Button, ListGroup} from 'react-bootstrap';
import '../css/main.css';
import PropTypes from 'prop-types';

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
                        <div>
                            <img
                                width={64}
                                height={64}
                                className="mr-3"
                                src="/img/test.jpg"
                                alt="Generic placeholder"
                            />


                            <a href="#profile" className="text--blue text--bold">{val.fullname}</a>
                            <a href="#rezfz" className="text--grey text--small">@{val.username}</a>
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

export default FollowingList;
