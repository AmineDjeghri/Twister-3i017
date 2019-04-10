import React, { Component } from 'react';
import {Form, Button} from  'react-bootstrap';
import PropTypes from 'prop-types';
import Cookies from "js-cookie";
import axios from "axios";
class CommentWrite extends Component {


    constructor(props){

        super(props)


        this.state={
            text:'',
            validated:false,
        }

    this.handleSubmit=this.handleSubmit.bind(this)
    }

    handleSubmit(event) {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        } else {
            const params = {
                key_session: Cookies.get('key_session'),
                id_message: this.props.twistId,
                text: this.text.value,
            }

            axios.post('http://localhost:8080/Twister/comment/add', null, {params})
                .then(res => {
                    console.log(res);
                    console.log(res.data);
                    this.props.refreshList()

                })

        }

        this.setState({validated: true});
        event.preventDefault()

    }


    render() {

        const { validated } = this.state;

        return (

                <Form noValidate
                      validated={validated}
                    onSubmit={ this.handleSubmit}>
                    <Form.Group controlId="validationCustom01">
                    <Form.Text className="text-muted">
                        Ajouter un commentaire
                    </Form.Text>
                    <Form.Control required ref={(text) => { this.text = text }}  type="text"  size="sm"  placeholder="Normal text" />
                    <Form.Control.Feedback  type="invalid">
                        Veuillez saisir un commentaire
                    </Form.Control.Feedback>
                    </Form.Group>
                    <Button className="comment-write_btn"  variant="outline-secondary" type="submit"  size="sm">
                        Publier
                    </Button>

                </Form>

        );
    }
}

export default CommentWrite;
