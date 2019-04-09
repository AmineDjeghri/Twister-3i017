import React, { Component } from 'react';
import './css/main.css';

class Logout extends Component {

  constructor(props){

      super(props)
      
  }


  render() {
    return (
      <div>
        <input type="submit" value ="Logout"/>
      </div>
    );
  }
}

export default Logout;
