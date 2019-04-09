import React, {Component, Fragment} from 'react';
import {Navbar, Form, Nav, NavDropdown, FormControl, Button, Image} from "react-bootstrap";
import {FaEnvelope, FaHome, FaSearch} from "react-icons/fa";
import { withRouter } from 'react-router';
import axios from "axios";
import links from "../utilities/navigationLinks";
import Cookies from "js-cookie";

class NavigationPannel extends Component {

    constructor(props){

        super(props)

        this.handleLogout=this.handleLogout.bind(this)

    }

    handleLogout(){

        const params={
            key_session:this.props.key_session

        }

        //ajouter d'autres conditions et vérifications !!

        axios.post('http://localhost:8080/Twister/user/logout',null, {params} )
            .then(res => {
                console.log(res);
                console.log(res.data);


                if (res.data.hasOwnProperty('Message of success'))
                    alert(res.data['Message of success'])
                else alert(res.data['Error Message'])


                Cookies.remove('isLoggedIn');
                Cookies.remove('key_session');
                Cookies.remove('login');
                Cookies.remove('id_user');

                this.props.logout()
                this.props.history.push(links.login)

            })

    }

    render() {

        const {isLoggedIn} =this.props

        return (

            <Navbar bg="white" expand="md" >
                <Navbar.Brand href={links.root}>Twister</Navbar.Brand>

                <Navbar.Toggle />

                <Navbar.Collapse>
                    <Nav className="mr-auto">
                        <Nav.Link href={links.root}  >
                            <FaHome/>
                            Accueil
                        </Nav.Link>

                        {isLoggedIn &&
                        <Nav.Link href="#link">
                            <FaEnvelope/>
                            Messages
                        </Nav.Link>
                        }
                    </Nav>

                    <Form inline className="navbar__search-form">
                        <FormControl type="text" placeholder="Search" className="mr-sm-2" />
                        <Button className="btn--blue">
                            <span>Search <FaSearch /> </span>
                        </Button>
                    </Form>

                    {isLoggedIn ?
                        <NavDropdown className="navbar__dropdown"
                                     title={
                                         <div >
                                             <Image className="navbar__pp" roundedCircle  src="/img/test.jpg"/>
                                         </div>
                                     }
                                     alignRight
                        >

                            <NavDropdown.Item href={links.profile}>Profil</NavDropdown.Item>
                            <NavDropdown.Item href="#messages">Messages</NavDropdown.Item>
                            <NavDropdown.Divider/>
                            <NavDropdown.Item  onClick={this.handleLogout}>Se déconnecter</NavDropdown.Item>
                        </NavDropdown>
                        :
                        <Fragment>
                            <Nav.Link className="btn--blue" href={links.signIn}> S'inscrire </Nav.Link>
                            <Nav.Link className="btn--white btn--rounded" href={links.login}> Se connecter </Nav.Link>
                        </Fragment>
                    }
                </Navbar.Collapse>
            </Navbar>
        );
    }
}

export default withRouter(NavigationPannel);
