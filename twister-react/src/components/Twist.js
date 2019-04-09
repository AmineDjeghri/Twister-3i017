import React, { Component } from 'react';
import PropTypes from 'prop-types';
import '../css/main.css';
import {Button,Dropdown,Card,ListGroup,Media} from 'react-bootstrap';
import {FaRegComment, FaRegHeart} from "react-icons/fa/index";
import CommentList from "./CommentList";
import Comment from "./Comment";
import CommentWrite from "./CommentWrite";

class Twist extends Component {

    static propTypes={
        twist:PropTypes.shape({
            text: PropTypes.string,
            date: PropTypes.string,
            fullName:PropTypes.string,
            login:PropTypes.string,
            likeCounter:PropTypes.number,
            commentCounter:PropTypes.number,

        }),

        isPostOwner: PropTypes.bool,
    }

    constructor(props) {
        super(props)

        this.state={
            twist:this.props.twist,
            isPostOwner:false,
        }

        // Binding functions to `this`
        this.handleDelete = this.handleDelete.bind(this)

    }


    handleDelete (evt)  {
    }

    render() {
        const{twist}=this.state


        return (

        <div className="twist">
            <Media className="twist__header">
                <img
                    width={64}
                    height={64}
                    className="mr-3"
                    src="/img/test.jpg"
                    alt="Generic placeholder"
                />
                <Media.Body>
                    <div >
                        <a href="#"className="text--blue text--bold text--small">{twist.fullName} </a>
                        <a href="#" className="text--grey text--small">{`@${twist.login}`}</a>
                        <span className="twist__date">{twist.date}</span>

                        <Dropdown className="twist__dropdown">
                            <Dropdown.Toggle className="twist__dropdown-toggle" variant="success" size="sm" id="dropdown-basic"/>
                            <Dropdown.Menu alignRight>
                                <Dropdown.Item href="#/action-1">Edit </Dropdown.Item>
                                <Dropdown.Item href="#/action-2">Delete </Dropdown.Item>
                                <Dropdown.Item href="#/action-3">Report</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                    </div>

                    <p className="twist__text">{twist.text} </p>
                    <div >
                        <Button className="twist__action-btn" variant="outline-primary" size="sm">
                            <FaRegHeart className="twist__action-icon"/>
                            <span className="twist__action-counter">{twist.likeCounter}</span>
                        </Button>
                        <Button className="twist__action-btn" variant="outline-primary" size="sm">
                            <FaRegComment className="twist__action-icon" />
                            <span className="twist__action-counter">{twist.commentCounter}</span>
                        </Button>

                    </div>
                    <CommentList className="twist__comment-list" comments={twist.comments}/>
                </Media.Body>
            </Media>

            <CommentWrite />
        </div>
        );
    }


}

export default Twist;