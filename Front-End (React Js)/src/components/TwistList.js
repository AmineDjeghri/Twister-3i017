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

    }





    render() {
        const{twists,refreshList,getProfile}=this.props
        return (
            <div>
                <Card >

                    <Card.Body>
                        <Card.Title>Twist Wall</Card.Title>
                        <TwistWrite refreshList={refreshList}/>
                    </Card.Body>


                    <ListGroup className="list-group-flush">
                        {twists.map((val) => (
                            <ListGroupItem key={val.id_message}>
                                <Twist showProfile={this.props.showProfile}  refreshList={refreshList} twist={val}  />
                            </ListGroupItem>
                        ))}
                    </ListGroup>

                </Card>

            </div>


        );
    }


}

export default TwistList;