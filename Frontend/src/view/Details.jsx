export default function Details({ data }) {
  function getDataView() {
    let res = [];
    data.forEach((value, index) => {
      console.log(index);
      res.push(
        <div key={index}>
          <div>Title: {value[1].title}</div>
          <div>Rating: {value[1].rating === "" ? 0 : value[1].rating}</div>
          <div>
            Review Count:{" "}
            {value[1].reviewCount === "" ? 0 : value[1].reviewCount}
          </div>
          <div>Current Price: {value[1].currentPrice}</div>
          <div>
            <a href={value[1].url} target="_blank">
              Link
            </a>
          </div>
        </div>
      );
    });
    return res;
  }
  return <div>{getDataView()}</div>;
}
