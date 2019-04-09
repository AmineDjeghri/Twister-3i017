import React, { Component } from 'react';
import {Form, Button, InputGroup} from 'react-bootstrap';
import '../css/main.css';
import PropTypes from 'prop-types';
import Cookies from 'js-cookie';
import axios from "axios";

class TwistWrite extends Component {

    static propTypes={
        text:PropTypes.string,

    }

    constructor(props){

        super(props)
        this.state={
            text:'',
            validated:false,
        }

        this.handleChange=this.handleChange.bind(this)
        this.handleSubmit=this.handleSubmit.bind(this)
    }

    handleChange(){
        this.setState({
            text:this.text.value,
        })
    }

    handleSubmit(event){
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }
        else {

            const params = {
                key_session: Cookies.get('key_session'),
                text:this.state.text,
            }

            axios.post('http://localhost:8080/Twister/twist/add',null, {params} )
                .then(res => {
                    console.log(res);
                    console.log(res.data);
                    this.props.updateWall();
                })





        }

        this.setState({ validated: true });

    }

    render() {
        const { validated } = this.state;

        return (
            <div>
                <Form  noValidate
                       validated={validated}
                       onSubmit={e => this.handleSubmit(e)}>
                    <Form.Group controlId="validationCustom01">
                        <Form.Text className="text-muted">
                            Publier quelque chose
                        </Form.Text>
                        <Form.Control required size="lg" type="text"  placeholder="Publier quelque chose"
                                      ref={(text) => { this.text = text }} onChange={this.handleChange}/>
                        <Form.Control.Feedback  type="invalid">
                            Veuillez saisir un texte avant de le twister
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Button className="btn--blue btn__twist-write" variant="primary" type="submit" >
                        Publier
                    </Button>

                </Form>

            </div>
        );
    }
}

export default TwistWrite;
