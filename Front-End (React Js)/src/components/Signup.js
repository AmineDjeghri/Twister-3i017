import React, { Component } from 'react';
import {Button, Form,InputGroup,Col} from "react-bootstrap";
import axios from "axios";
import links from "../utilities/navigationLinks";
import {Redirect} from "react-router-dom";

class Signup extends Component {

  constructor(props){

    super(props)

    this.state = { validated: false,
      redirect:false, //to redirect to the login page if submit succeeded
      firstName:'',
      lastName:'',
      login:'',
      email:'',
      password:''};

    this.handleSubmit=this.handleSubmit.bind(this)
    this.handleChange=this.handleChange.bind(this)

  }

  handleSubmit(event) {
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }
    else{

      const params=
          { firstName:this.state.firstName,
            lastName:this.state.lastName,
            email:this.state.email,
            login:this.state.login,
            password:this.state.password}


      axios.post('http://localhost:8080/Twister/user/create',null, {params} )
          .then(res => {
            console.log(res);
            console.log(res.data);
          })

      //to redirect
      this.setState({
        redirect:true,
      })

      alert('Inscription effectée avecc succès')
    }

    this.setState({ validated: true });




  }

  handleChange(){
    this.setState({
      firstName:this.firstName.value,
      lastName:this.lastName.value,
      email:this.email.value,
      password:this.password.value,
      login:this.login.value,

    });
  }


  render() {

    const { validated,redirect } = this.state;

    if (redirect) {
      return <Redirect to={links.login}/>;
    }

    return (
        <div className="signup">


          <Form
              noValidate
              validated={validated}
              onSubmit={e => this.handleSubmit(e)}
          >

            <div className="signup__header">
              <h1>S'inscrire sur Twister</h1>
            </div>

            <Form.Row>
              <Form.Group as={Col} md="4" controlId="validationCustom01">
                <Form.Label className="text--small">Prénom</Form.Label>
                <Form.Control
                    required
                    type="text"
                    placeholder="Prénom"
                    size="sm"
                    ref={(firstName) => { this.firstName = firstName }} onChange={this.handleChange}
                />
                <Form.Control.Feedback>Correcte</Form.Control.Feedback>
              </Form.Group>
              <Form.Group as={Col} md="4" controlId="validationCustom02">
                <Form.Label className="text--small">Nom</Form.Label>
                <Form.Control
                    required
                    type="text"
                    placeholder="Nom"
                    size="sm"
                    ref={(lastName) => { this.lastName = lastName }} onChange={this.handleChange}
                />
                <Form.Control.Feedback>Correcte!</Form.Control.Feedback>
              </Form.Group>
            </Form.Row>


            <Form.Row>

              <Form.Group as={Col} md="4" controlId="validationCustomUsername">
                <Form.Label className="text--small"> Nom d'utilisateur</Form.Label>
                <InputGroup size="sm">
                  <InputGroup.Prepend >
                    <InputGroup.Text id="inputGroupPrepend">@</InputGroup.Text>
                  </InputGroup.Prepend>
                  <Form.Control
                      type="text"
                      placeholder="Nom d'utilisateur"
                      aria-describedby="inputGroupPrepend"
                      required
                      ref={(login) => { this.login = login }} onChange={this.handleChange}
                  />
                  <Form.Control.Feedback  type="invalid">
                    Veuillez choisir un nom d'utilisateur
                  </Form.Control.Feedback>
                </InputGroup>
              </Form.Group>
            </Form.Row>

            <Form.Row>
              <Form.Group as={Col} md="6"  controlId="formBasicEmail">
                <Form.Label className="text--small">Email</Form.Label>
                <Form.Control
                    type="email"
                    placeholder="Email"
                    size="sm"
                    required
                    ref={(email) => { this.email = email }} onChange={this.handleChange}
                />
                <Form.Control.Feedback type="invalid">
                  Veuillez saisir votre email
                </Form.Control.Feedback>
              </Form.Group>
            </Form.Row>

            <Form.Row >

              <Form.Group as={Col} md="4" controlId="formBasicPassword">
                <Form.Label className="text--small">Mot de passe</Form.Label>
                <Form.Control
                    type="password"
                    placeholder="mot de passe"
                    size="sm"
                    required
                    ref={(password) => { this.password = password }} onChange={this.handleChange}
                />
                <Form.Control.Feedback type="invalid">
                  Veuillez saisir un mot de passe
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group as={Col} md="4" controlId="formBasicPassword">
                <Form.Label className="text--small">Confirmer le mot de passe</Form.Label>
                <Form.Control
                    type="password"
                    placeholder=" mot de passe"
                    size="sm"
                    required
                />
                <Form.Control.Feedback type="invalid">
                  Veuillez confirmer votre mot de passe
                </Form.Control.Feedback>
              </Form.Group>

            </Form.Row>



            <Button className="btn--blue"  type="submit" >
              S'inscrire
            </Button>
          </Form>

          <div className="login__footer">
            <span className="text--grey text--small">
              Vous avez déja un compte ?
              <a className=" text--blue " href={links.login} > Se connecter</a>
            </span>

          </div>
        </div>
    );
  }
}

export default Signup;
