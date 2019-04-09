import React, { Component } from 'react';
import {Form, Button} from  'react-bootstrap';
import PropTypes from 'prop-types';
class CommentWrite extends Component {


    static propTypes={
        text:PropTypes.string,

    }


    constructor(props){

        super(props)

    }



    render() {
        return (

                <Form>
                    <Form.Text className="text-muted">
                        Ajouter un commentaire
                    </Form.Text>
                    <Form.Control type="text"  size="sm"  placeholder="Normal text" />
                    <Button className="comment-write_btn" variant="outline-secondary" type="submit"  size="sm">
                        Publier
                    </Button>

                </Form>

        );
    }
}

export default CommentWrite;
