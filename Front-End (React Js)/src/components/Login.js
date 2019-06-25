import React, { Component } from 'react';
import {Form, Button, InputGroup,} from "react-bootstrap";
import Cookies from 'js-cookie';
import axios from 'axios';

import '../css/main.css';
import links from "../utilities/navigationLinks";

class Login extends Component {

    constructor(props){

        super(props)
        this.state = {
            login: "",
            password: "",
            validated:false,
        }

        this.handleChange=this.handleChange.bind(this)

    }



    handleChange(){
        this.setState({
            login:this.login.value,
            password:this.password.value,

        })
    }

    handleSubmit = event => {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }
        else {
            const params = {
                login: this.state.login,
                password: this.state.password,
            }


            axios.post('http://localhost:8080/Twister/user/login', null, {params})
                .then(res => {
                    console.log(res);
                    console.log(res.data);

                    //Test for example if the res.data has the property key_session
                    if (res.data.hasOwnProperty('key_session')) {
                        let user = res.data
                        Cookies.set('isLoggedIn', true);
                        Cookies.set('key_session', res.data.key_session);
                        Cookies.set('id_user', res.data.id_user);
                        this.props.login(user)

                    } else alert(res.data['Error Message'])

                })

        }
        event.preventDefault(); //prevent default behaviour of the form
        this.setState({ validated: true });

    }

    componentDidMount() {

    }

    render() {
        const { validated } = this.state;

        return (
            <div className="login">
                <Form className="login__form"
                      noValidate
                      validated={validated}
                      onSubmit={e => this.handleSubmit(e)}>

                    <div className="login__header">
                        <h1>Se connecter à Twister</h1>
                    </div>

                    <Form.Group  controlId="validationCustomUsername">
                        <Form.Label >Nom d'utilisateur</Form.Label>
                        <InputGroup size="sm">
                        <InputGroup.Prepend >
                            <InputGroup.Text id="inputGroupPrepend">@</InputGroup.Text>
                        </InputGroup.Prepend>
                        <Form.Control required placeholder="Nom d'utilisateur" size="sm"
                                      ref={(login) => { this.login = login }} onChange={this.handleChange}/>
                        </InputGroup>
                        <Form.Text className="text-muted">
                            Ne donnez jamais votre mot de passe à quelqu'un d'autre
                        </Form.Text>
                        <Form.Control.Feedback  type="invalid">
                            Veuillez saisir votre email
                        </Form.Control.Feedback>

                    </Form.Group>

                    <Form.Group controlId="formBasicPassword">
                        <Form.Label>Mot de passe</Form.Label>
                        <InputGroup size="sm">
                        <Form.Control required type="password" placeholder="mot de passe" size="sm"
                                      ref={(password) => { this.password = password }} onChange={this.handleChange}/>
                        </InputGroup>
                        <Form.Control.Feedback  type="invalid">
                            Veuillez saisir un mot de passe
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Button className="btn--blue" variant="primary" type="submit" >
                        Se connecter
                    </Button>

                    <a href={links.recoverpassword} className="text--blue text--small"> Mot de passe oublié ?</a>
                </Form>

                <div className="login__footer">
              <span className="text--grey text--small">
                  Pas encore inscrit ?
                  <a className=" text--blue " href={links.signIn} > S'inscrire ici</a>
              </span>

                </div>

            </div>
        );
    }
}

export default Login;
