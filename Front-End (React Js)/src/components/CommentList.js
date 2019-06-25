import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Comment from "./Comment";
import Twist from "./Twist";



class CommentList extends Component {

    static propTypes={
        comments:PropTypes.arrayOf(PropTypes.object),

    }

    constructor(props) {
        super(props)

    }


    render() {
        const{comments,refreshList}=this.props

        return (
            <div className="comment-list">
                {comments.map((val, index) => <Comment showProfile={this.props.showProfile}   refreshList={refreshList} comment={val} twistId={this.props.twistId} key={index} />)}
            </div>


        );
    }


}

export default CommentList;