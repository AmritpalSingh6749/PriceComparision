import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import Header from "./view/Header";
import Form from "./view/Form";
import "./App.css";
import Details from "./view/Details";
import OrderForm from "./view/Order";

function App() {
  const websiteList = ["Flipkart", "SnapDeal", "ShopClues"];
  const [details, setDetails] = useState({});
  const [order, setOrder] = useState("none");
  const [orderBy, setOrderBy] = useState("none");

  function compare(a, b) {
    if (a.length === b.length) return a.localeCompare(b);
    return a.length > b.length;
  }

  function sortFun(a, b) {
    // console.log(a, b);
    if (orderBy === "price") {
      return compare(a[1].currentPrice, b[1].currentPrice);
    } else if (orderBy === "rating") {
      return compare(a[1].rating, b[1].rating);
    } else if (orderBy === "review")
      return compare(a[1].reviewCount, b[1].reviewCount);
  }

  function detailsBlock(blockDetails) {
    let res = [];
    // console.log("Re-render happening");
    Object.entries(blockDetails).forEach(([key, value]) => {
      value =
        order === "none" ? value : Object.entries({ ...value }).sort(sortFun);
      value = order === "desc" ? value.reverse() : value;
      console.log({ ...value });
      res.push(
        <div>
          <h2>{key}</h2>
          <Details data={value} />
        </div>
      );
    });
    return res;
  }
  function productDetails(data) {
    console.log(data);
    setDetails(data);
  }
  function sortDetails(sortOrder, sortOrderBy) {
    console.log(sortOrder, sortOrderBy);
    setOrder(sortOrder);
    setOrderBy(sortOrderBy);
  }
  return (
    <div>
      <Header />
      <Form websites={websiteList} dataSetter={productDetails} />
      <OrderForm orderSetter={sortDetails} />
      {detailsBlock(details)}
    </div>
  );
}

export default App;
