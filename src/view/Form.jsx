import { useEffect, useState } from "react";
import axios from "axios";

export default function Form({ websites, dataSetter }) {
  const [searchVal, setSearchVal] = useState("");
  const [resultCount, setResultCount] = useState(5);
  const [searchSite, setSearchSite] = useState([]);
  const [submitButton, setSubmitButton] = useState(true);

  function checkBoxHandler(event) {
    console.log(searchSite);
    if (event.target.checked)
      setSearchSite((data) => [...data, event.target.value]);
    else {
      var index = searchSite.indexOf(event.target.value);
      if (index > -1) {
        searchSite.splice(index, 1);
      }
      setSearchSite(searchSite);
    }
  }

  function searchHandler(event) {
    const val = event.target.value;
    if (val.length > 2) setSubmitButton(false);
    else setSubmitButton(true);
    console.log(searchVal);
    setSearchVal(val);
  }
  function createChecBox(website) {
    return (
      <div>
        <input
          type="checkbox"
          value={website}
          name={website}
          id="checkBox"
          onChange={checkBoxHandler}
        />
        <label htmlFor={website}>{website}</label>
      </div>
    );
  }

  function searchCounthandler(event) {
    setResultCount(event.target.value);
  }

  function submitHandler(event) {
    event.preventDefault();
    const url =
      "https://16b4-59-89-81-85.ngrok.io/api/v1/search?k=" +
      searchVal +
      "&n=" +
      resultCount +
      "&s=" +
      searchSite;
    console.log(url);
    axios
      .get(url)
      .then((res) => dataSetter(res.data.response))
      .catch((error) => {
        if (error.response) console.log(error.response.data);
        else if (error.request) console.log(error.request);
        else console.log(error.message);
      });
  }
  return (
    <div>
      <form onSubmit={submitHandler}>
        <div>
          <p>Enter Keyword to Search (Minimum 3 character): </p>
          <input
            type="text"
            id="search"
            value={searchVal}
            onChange={searchHandler}
          />
        </div>
        <div>
          <p>Number fo results you would like to fetch (default 5): </p>
          <input
            type="number"
            id="results"
            value={resultCount}
            onChange={searchCounthandler}
          />
        </div>
        <div>
          <p>Select Website (Default all):</p>
          <div>{websites.map((website) => createChecBox(website))}</div>
        </div>
        <div>
          <input type="submit" value="Submit" disabled={submitButton} />
        </div>
      </form>
    </div>
  );
}
