import React, { Component } from 'react';
import {Card, Button, Row, Col} from "react-bootstrap";
import '../css/main.css';

class DashboardProfileCard extends Component {

    constructor(props){

        super(props)

    }


    render() {
        return (
            <Card className="dashboard-profile">
                <Card.Img className="dashboard-profile__bg " variant="top" src="/img/test.jpg" />
                <Card.Body  className="d-flex align-items-center justify-content-around">
                    <Card.Img className="dashboard-profile__pp "  src="/img/test.jpg" />
                    <div>
                        <a href="#profile" className="text--blue text--bold">Moh Laka</a>
                        <a href="#" className="text--grey text--small">@lakalaka</a>
                    </div>
                </Card.Body>
                <Card.Footer className="bg-white">

                    <Row>
                        <Col>
                            <div className="d-flex flex-column text--small font-weight-bold">
                                <Card.Link className="text--blue" href="#zada">Twists</Card.Link>
                                <Card.Link className="text--blue" href="#eae">25</Card.Link>
                            </div>
                        </Col>
                        <Col>
                            <div className="d-flex flex-column text--small font-weight-bold">
                                <Card.Link className="text--blue" href="#zada">Twists</Card.Link>
                                <Card.Link className="text--blue" href="#eae">25</Card.Link>
                            </div>
                        </Col>
                        <Col>
                            <div className="d-flex flex-column text--small font-weight-bold">
                                <Card.Link className="text--blue" href="#zada">Twists</Card.Link>
                                <Card.Link className="text--blue" href="#eae">25</Card.Link>
                            </div>
                        </Col>
                    </Row>


                </Card.Footer>
            </Card>
        );
    }
}

export default DashboardProfileCard;
