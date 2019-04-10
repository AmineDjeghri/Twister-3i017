import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Comment from "./Comment";



class CommentList extends Component {

    static propTypes={
        comments:PropTypes.arrayOf(PropTypes.object),

    }

    constructor(props) {
        super(props)

    }


    render() {
        const{comments}=this.props

        console.log('constructor appelé '+comments.length)
        return (
            <div className="comment-list">
                {comments.map((val, index) => <Comment comment={val} twistId={this.props.twistId} key={index} />)}
            </div>


        );
    }


}

export default CommentList;