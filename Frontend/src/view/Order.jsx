import { useState } from "react";
export default function OrderForm({ orderSetter }) {
  const [order, setOrder] = useState("none");
  const [orderBy, setOrderBy] = useState("none");

  function orderHandler(event) {
    setOrder(event.target.value);
  }

  function orderByHandler(event) {
    setOrderBy(event.target.value);
  }

  function submitHandler(event) {
    event.preventDefault();
    orderSetter(order, orderBy);
  }

  return (
    <form onSubmit={submitHandler}>
      <div>
        <p>Select Order (default none): </p>
        <select defaultValue={order} id="order" onChange={orderHandler}>
          <option value="none">None</option>
          <option value="asc">Ascending</option>
          <option value="desc">Descending</option>
        </select>
      </div>
      <div>
        <p>Select Order by (default none): </p>
        <select value={orderBy} id="orderBy" onChange={orderByHandler}>
          <option value="none">None</option>
          <option value="price">Price</option>
          <option value="rating">Rating</option>
          <option value="review">Review Count</option>
        </select>
      </div>
      <div>
        <input type="submit" value="Submit" />
      </div>
    </form>
  );
}
