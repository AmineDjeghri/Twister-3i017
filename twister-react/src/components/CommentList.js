import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Comment from "./Comment";



class CommentList extends Component {

    static propTypes={
        commentList:PropTypes.arrayOf(PropTypes.object),

    }

    constructor(props) {
        super(props)

        this.state={
            comments:this.props.comments,

        }

        // Binding functions to `this`

    }


    render() {
        const{comments}=this.state
        return (
            <div className="comment-list">
                {comments.map((val, index) => <Comment comment={val} key={index} />)}
            </div>


        );
    }


}

export default CommentList;