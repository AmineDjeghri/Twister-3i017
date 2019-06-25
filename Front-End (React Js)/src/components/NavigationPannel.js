import React, {Component, Fragment} from 'react';
import {Navbar, Form, Nav, NavDropdown, FormControl, Button, Image, Col} from "react-bootstrap";
import {FaEnvelope, FaHome, FaSearch} from "react-icons/fa";
import { withRouter } from 'react-router';
import axios from "axios";
import links from "../utilities/navigationLinks";
import Cookies from "js-cookie";

class NavigationPannel extends Component {

    constructor(props){

        super(props)

        this.state={
            validated:false,
            search:"",
        }

        this.handleLogout=this.handleLogout.bind(this)
        this.handleSearch=this.handleSearch.bind(this)
        this.handleChange=this.handleChange.bind(this)
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
                Cookies.remove('id_user');

                this.props.logout()
                this.props.history.push(links.login)

            })

    }

    handleSearch = event => {
        const form = event.currentTarget;

        const params = {
            key_session: Cookies.get('key_session'),
            recherche:this.state.search,
        }

        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }

        else {
            axios.get('http://localhost:8080/Twister/user/search?' + new URLSearchParams(params))
                .then(res => {
                    console.log(res);
                    console.log(res.data);

                    this.props.search(res.data.users)
                })
        }
        event.preventDefault(); //prevent default behaviour of the form
        this.setState({ validated: true });

    }
    handleChange(){
        this.setState({
            search:this.search.value,
        })
    }

    render() {
        const { validated } = this.state;
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

                    </Nav>



                    {isLoggedIn &&
                    <Form
                        noValidate
                        validated={validated}
                        onSubmit={e => this.handleSearch(e)}
                        inline className="navbar__search-form">
                        <Form.Group controlId="validationCustom01">
                        <FormControl required type="text" placeholder="Search a user or a twist"
                                     ref={(search) => { this.search = search }} onChange={this.handleChange}
                                     className="mr-sm-2"/>
                        </Form.Group>

                        {validated ?
                        <Button type="submit"  href={ links.search} className="btn--blue">
                            <span> Search <FaSearch/> </span>
                        </Button>:
                        <Button type="submit"   className="btn--blue">
                            <span> Search <FaSearch/> </span>
                        </Button> }
                    </Form>
                    }



                    {isLoggedIn ?
                        <NavDropdown className="navbar__dropdown"
                                     title={
                                         <div >
                                             <Image className="navbar__pp" roundedCircle  src="/img/test.jpg"/>
                                         </div>
                                     }
                                     alignRight
                        >

                            <NavDropdown.Item onClick={Cookies.remove('l_p')} href={links.profile}>Profil</NavDropdown.Item>
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
