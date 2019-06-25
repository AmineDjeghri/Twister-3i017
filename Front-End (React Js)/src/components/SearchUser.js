import React, { Component } from 'react';
import {Form, InputGroup, Button, ListGroup} from "react-bootstrap";
import '../css/main.css';

import links from "../utilities/navigationLinks";
import axios from "axios";
import Cookies from "js-cookie";
import {Link} from "react-router-dom";

class SearchUser extends Component {

    constructor(props){
        super(props)

        this.getProfile=this.getProfile.bind(this)

    }


    //c'est le seul truc ou nous n'avons pas trouvé un autre moyen que les cookies pour aller au profile désirer
    getProfile(login){
        this.props.showProfile(login)
        console.log('search user .js getprofile called')
    }

    render() {

        const {searchList}=this.props
        return (
            <ListGroup >

                {searchList.map((val, index) =>
                    <ListGroup.Item className="d-flex justify-content-around" key={index} >
                        <img
                            width={64}
                            height={64}
                            className="mr-3"
                            src="/img/test.jpg"
                            alt="Generic placeholder"
                        />

                        <Link  to={links.profile} onClick={()=>this.getProfile(val.login_user)} className="text--blue text--bold">{val.first_name_user} {val.family_name_user}</Link>
                        <a href={links.profile} className="text--grey text--small">@{val.login_user}</a>

                    </ListGroup.Item>)

                }

            </ListGroup>
        );
    }
}

export default SearchUser;