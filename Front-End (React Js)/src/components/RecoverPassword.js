import React, { Component } from 'react';
import {Form, InputGroup, Button} from "react-bootstrap";
import '../css/main.css';

import links from "../utilities/navigationLinks";

class RecoverPassword extends Component {

    constructor(props){
        super(props)
    }

    render() {
        return (
            <div className="recoverpassword">
                <Form className="recoverpassword__form">
                     // onSubmit={e => this.handleSubmit(e)}>
                    <div className="recoverpassword__header">
                        <h1>Retrouvez votre compte</h1>
                        <p>
                            Veuillez saisir votre login et votre adresse e-mail pour rechercher votre compte.
                        </p>
                    </div>

                    <Form.Group  controlId="validationCustomUsername">
                        <Form.Label >Nom d'utilisateur</Form.Label>
                        <InputGroup size="sm">
                            <InputGroup.Prepend >
                                <InputGroup.Text id="inputGroupPrepend">@</InputGroup.Text>
                            </InputGroup.Prepend>
                            <Form.Control required placeholder="Nom d'utilisateur" size="sm">
                                // fonction handler je pense.
                            </Form.Control>
                        </InputGroup>
                    </Form.Group>

                    <Form.Group controlId="formBasicPassword">
                        <Form.Label>Email</Form.Label>
                        <InputGroup size="sm">
                            <Form.Control required type="email" placeholder="email" size="sm">
                                // fonction handler aussi
                            </Form.Control>
                        </InputGroup>
                        <Form.Control.Feedback  type="invalid">
                            Veuillez saisir votre email
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Button className="btn--blue" variant="primary" type="submit" >
                        Valider
                    </Button>
                    <Button className="btn--blue" variant="primary" type="submit" >
                        Valider
                    </Button>

                    <a href={links.login} className="text--blue text--small"> Se connecter </a>
                </Form>

                <div className="login__footer">
              <span className="text--grey text--small">
                  Merci de vérifier que vous avez reçu un e-mail avec votre mot de passe.
              </span>

                </div>

            </div>
        );
    }
}

export default RecoverPassword;