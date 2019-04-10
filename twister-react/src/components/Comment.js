import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {Button, Dropdown, Media} from 'react-bootstrap';
import {FaRegComment, FaRegHeart} from "react-icons/fa/index";
import Cookies from "js-cookie";
import axios from "axios";

class Comment extends Component {

    static propTypes={
        comment:PropTypes.shape({
            text: PropTypes.string,
            date: PropTypes.string,
            likeCounter:PropTypes.number,
            fullname:PropTypes.string,
            username:PropTypes.string,
        }),
        isPostOwner: PropTypes.bool,
        isCommentOwner: PropTypes.bool,
    }

    constructor(props) {
        super(props)

        this.state={
            comment:this.props.comment,
            isPostOwner:false,
            isCommentOwner:false,
        }

        // Binding functions to `this`
        this.handleDelete = this.handleDelete.bind(this)

    }


    handleDelete ()  {
        const params={
            key_session:Cookies.get('key_session'),
            id_message:this.props.twistId,
            id_comment:this.props.comment.id_comment,
        }

        axios.delete('http://localhost:8080/Twister/comment/remove',{params})
            .then(res => {
                console.log(res);
                console.log(res.data);

            })

    }



    render() {
        const comment=this.state.comment
        return (


            <div className="comment">
                <Media className="twist__header">
                    <img
                        width={50}
                        height={50}
                        className="mr-3"
                        src="/img/test.jpg"
                        alt="Generic placeholder"
                    />
                    <Media.Body>
                        <div >
                            <a href="#"className="text--blue text--bold text--small">{comment.fullName} </a>
                            <a href="#" className="text--grey text--small">{`@${comment.login}`}</a>
                            <span className="twist__date">{comment.date}</span>

                            <Dropdown className="twist__dropdown">
                                <Dropdown.Toggle className="twist__dropdown-toggle" variant="success" size="sm" id="dropdown-basic"/>
                                <Dropdown.Menu alignRight>
                                    <Dropdown.Item href="#/action-1">Edit </Dropdown.Item>
                                    <Dropdown.Item onClick={this.handleDelete}>Delete </Dropdown.Item>
                                    <Dropdown.Item href="#/action-3">Report</Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </div>

                        <p className="twist__text">{comment.text} </p>
                        <div >
                            <Button className="twist__action-btn" variant="outline-primary" size="sm">
                                <FaRegHeart className="twist__action-icon"/>
                                <span className="twist__action-counter">{comment.likeCounter}</span>
                            </Button>
                            <Button className="twist__action-btn" variant="outline-primary" size="sm">
                                <FaRegComment className="twist__action-icon" />
                                <span className="twist__action-counter">{comment.commentCounter}</span>
                            </Button>

                        </div>

                    </Media.Body>
                </Media>
            </div>
        );
    }


}

export default Comment;