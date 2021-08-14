// list for doing stuff
//Created by Kirk Hietpas and Tyler Jette August 2021
var category_colors = [
  "#90e0ef",
  "#48cae4",
  "#00b4d8",
  "#0096c7",
  "#0077b6",
  "#023e8a",
  "#03045e",
];

var category_numbers = [2500, 5000, 10000, 20000, 40000, 60000];

var category_str = [
  "< 2500",
  "< 5000",
  "< 10000",
  "< 20000",
  "< 40000",
  "< 60000",
  ">= 60000",
];

window.onload = async function () {
  // main function called onload
  getData(
    // pull data from CDC website
    "https://data.cdc.gov/resource/9mfq-cb36.json?$limit=50000",
    (infection_rate_data) =>
      (function () {
        getData(
          "https://data.cdc.gov/resource/r8kw-7aab.json?$limit=50000",
          (mortality_data) => main(infection_rate_data, mortality_data)
        );
      })()
  );
};

async function main(infection_data, mortality_data) {
  // main function where everything happens
  // create some list to store data where I want it
  data_by_state_weekly = {};
  data_by_state_monthly = {};
  data_by_state_yearly = {};
  data_by_state_total = {};
  data_by_state_4wk_rate = {};

  var today = new Date(); // get today's date
  preProcess(
    data_by_state_weekly,
    data_by_state_monthly,
    data_by_state_yearly,
    data_by_state_total,
    data_by_state_4wk_rate,
    mortality_data
  );

  // getting data to create map
  const us = await d3.json("https://d3js.org/us-10m.v2.json");
  const map_data = topojson.feature(us, us.objects.states).features;

  addProps(
    map_data,
    data_by_state_total,
    data_by_state_yearly,
    data_by_state_weekly,
    data_by_state_monthly,
    data_by_state_4wk_rate
  ); // add properties to the mapData

  const width = 960;
  const height = 600;

  var statePointerLabel = d3
    .select("#map")
    .append("div")
    .attr("class", "pointerCanvas")
    .style("opacity", 0)
    .style("padding-left", "12px")
    .style("padding-right", "12px");

  const svg = d3
    .select("#map")
    .append("svg")
    .attr("width", width)
    .attr("height", height);

  const path = d3.geoPath();

  renderMap(null, svg, map_data, path, statePointerLabel); // first time rendering
  createLegend();
}

//***************************** */
// pre process all the data

function addProps(
  map_data,
  data_by_state_total,
  data_by_state_yearly,
  data_by_state_weekly,
  data_by_state_monthly,
  data_by_state_4wk_rate
) {
  for (var s in map_data) {
    // for total deaths
    map_data[s].properties.totalDeaths =
      data_by_state_total[map_data[s].properties.name];

    // for yearly
    map_data[s].properties.yearly_data =
      data_by_state_yearly[map_data[s].properties.name];
    // weekly
    map_data[s].properties.weekly_data =
      data_by_state_weekly[map_data[s].properties.name];

    // monthly
    map_data[s].properties.monthly_data =
      data_by_state_monthly[map_data[s].properties.name];

    // 4wk
    map_data[s].properties.fourWeek_data =
      data_by_state_4wk_rate[map_data[s].properties.name];
  }
}

function preProcess(
  data_by_state_weekly,
  data_by_state_monthly,
  data_by_state_yearly,
  data_by_state_total,
  data_by_state_4wk_rate,
  mortality_data
) {
  for (var i in mortality_data) {
    //filter out undefined data for "covid_19_deaths" field
    if (mortality_data[i]["covid_19_deaths"] != undefined) {
      //filter out the following geographic regions
      if (
        mortality_data[i]["state"] != "United States" &&
        mortality_data[i]["state"] != "New York City" &&
        mortality_data[i]["state"] != "Puerto Rico"
      ) {
        var state = mortality_data[i]["state"];
        if (state == "New Hamsphire") {
          console.log(mortality_data[i]);
        }
        //determine if the data is weekly or monthly, or yearly
        if (mortality_data[i]["group"] == "By Week") {
          if (state in data_by_state_weekly) {
            data_by_state_weekly[state][mortality_data[i]["end_date"]] =
              mortality_data[i]["covid_19_deaths"];
          } else {
            data_by_state_weekly[state] = {};
            data_by_state_weekly[state][mortality_data[i]["end_date"]] =
              mortality_data[i]["covid_19_deaths"];
          }
        } else if (mortality_data[i]["group"] == "By Month") {
          if (state in data_by_state_monthly) {
            data_by_state_monthly[state][mortality_data[i]["end_date"]] =
              mortality_data[i]["covid_19_deaths"];
          } else {
            data_by_state_monthly[state] = {};
            data_by_state_monthly[state][mortality_data[i]["end_date"]] =
              mortality_data[i]["covid_19_deaths"];
          }
        } else if (mortality_data[i]["group"] == "By Year") {
          if (state in data_by_state_yearly) {
            data_by_state_yearly[state][mortality_data[i]["end_date"]] =
              mortality_data[i]["covid_19_deaths"];
          } else {
            data_by_state_yearly[state] = {};
            data_by_state_yearly[state][mortality_data[i]["end_date"]] =
              mortality_data[i]["covid_19_deaths"];
          }
        } else if (mortality_data[i]["group"] == "By Total") {
          data_by_state_total[state] = mortality_data[i]["covid_19_deaths"];
        }
      } //end of geo filter
    } //end of undefined data filter
  } //end of preprocessing loop
}

//Functions
function getData(url, callback) {
  fetch(url)
    .then((response) => response.json())
    .then((result) => callback(result));
}

function renderMap(selected, svg, map_data, path, statePointerLabel) {
  svg
    .append("g")
    .selectAll("path")
    .data(map_data)
    .enter()
    .append("path")
    .attr("d", path)
    .style("fill", function (s) {
      if (s.properties.name == selected) {
        return "red";
      } else {
        var td = s.properties.totalDeaths;
        if (td < 2500) {
          return "#90e0ef";
        } else if (td < 5000) {
          return "#48cae4";
        } else if (td < 10000) {
          return "#00b4d8";
        } else if (td < 20000) {
          return "#0096c7";
        } else if (td < 40000) {
          return "#0077b6";
        } else if (td < 60000) {
          return "#023e8a";
        } else {
          return "#03045e";
        }
      }
    })
    .on("mouseover", function (e, d) {
      statePointerLabel.transition().duration(500).style("opacity", 1);
      statePointerLabel
        .text(d.properties.name + " : " + d.properties.totalDeaths)
        .style("left", (d3.pointer(e)[0] + 30).toString() + "px")
        .style("top", d3.pointer(e)[1] + "px");
    })
    .on("mouseout", function (d) {
      //fadeout
      statePointerLabel.transition().duration(500).style("opacity", 0);
    })
    .on("click", function (e, d) {
      handleClick(e, d);
      renderMap(d.properties.name, svg, map_data, path, statePointerLabel); //recurse
    });
} //end of renderMap()

function createLegend() {
  for (i in category_colors) {
    d3.select("#legend")
      .append("div")
      .style("height", "12px")
      .style("width", "25px")
      .style("background-color", category_colors[i])
      .style("display", "inline-block");

    d3.select("#legend")
      .append("div")
      .style("margin-left", "5px")
      .style("width", "80px")
      .text(category_str[i])
      .style("display", "inline-block");
  }
}

function handleClick(e, d) {
  let graph = d3.select("#graph");
  graph.selectAll("p").remove(); // clear everything that was there
  graph.selectAll("h1").remove(); // clear everything that was there

  var yearEntrys = Object.entries(d.properties.yearly_data); // grab yearly
  var year1 = yearEntrys[0];
  var year2 = yearEntrys[1];

  // append state name
  graph.append("h1").text(d.properties.name).style("font-weight", "bold");

  // append yearly data
  graph
    .append("p")
    .text("\nYear: " + year1[0].substr(0, 4) + " Yearly Deaths " + year1[1]);

  graph
    .append("p")
    .text("\nYear: " + year2[0].substr(0, 4) + " Yearly Deaths " + year2[1]);

  // weekly avg
  var weeklyAvg = avgPer(Object.values(d.properties.weekly_data));
  graph.append("p").text("Weekly Average: " + weeklyAvg);

  // monthly avg
  var monthlyAvg = avgPer(Object.values(d.properties.monthly_data));
  graph.append("p").text("Monthly Average: " + monthlyAvg);

  createGraph(e, d);
}

function avgPer(vals) {
  var count = 0;
  var sum = 0;
  for (var i in vals) {
    sum += parseInt(vals[i]); // this was a string
    count++;
  }
  return Math.round(sum / count);
}

function createGraph(e, d) {
  //   //www.educative.io/edpresso/how-to-create-a-line-chart-using-d3
  // set the dimensions and margins of the graph ?? do i need these
  let prev = d3.select("#lineGraph");
  prev.remove();

  var margin = { top: 10, right: 30, bottom: 50, left: 60 },
    width = 460 - margin.left - margin.right,
    height = 400 - margin.top - margin.bottom;

  // append the svg object to the body of the page
  var graphSvg = d3
    .select("#graph")
    .append("svg")
    .attr("id", "lineGraph")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  var formatedList = formatTimeVars(d.properties.weekly_data);

  var x = d3
    .scaleTime()
    .domain(
      d3.extent(formatedList, function (d) {
        return d.date;
      })
    )
    .range([0, width]);

  var xAxis = graphSvg
    .append("g")
    .attr("transform", "translate(0," + height + ")")
    .attr("class", "xLabel")
    .call(d3.axisBottom(x));

  // Add Y axis
  var y = d3
    .scaleLinear()
    .domain([
      0,
      d3.max(formatedList, function (d) {
        return +d.deaths;
      }),
    ])
    .range([height, 0]);
  var yAxis = graphSvg.append("g").attr("class", "yLabel").call(d3.axisLeft(y));

  // transition stuff
  const transitionPath = d3.transition().ease(d3.easeSin).duration(3500);

  // Draw the line
  graphSvg
    .append("path")
    .datum(formatedList)
    .transition(transitionPath) // add transition
    .attr("fill", "none")
    .attr("stroke", "red")
    .attr("stroke-width", 1.5)
    .attr(
      "d",
      d3
        .line()
        .x(function (d) {
          return x(d.date);
        })
        .y(function (d) {
          return y(d.deaths);
        })
    );

  // text label for the x axis
  graphSvg
    .append("text")
    .attr(
      "transform",
      "translate(" + width / 2 + " ," + (height + margin.top + 20) + ")"
    )
    .style("text-anchor", "middle")
    .text("Date");

  // text label for the y axis
  graphSvg
    .append("text")
    .attr("transform", "rotate(-90)")
    .attr("y", 0 - margin.left)
    .attr("x", 0 - height / 2)
    .attr("dy", "1em")
    .style("text-anchor", "middle")
    .text("Deaths per week in " + d.properties.name);
}

//format the date
function formatTimeVars(weekData) {
  var arr = [];
  for (var time in weekData) {
    var parsed = new Date(time); // just get the dat
    var intDeaths = parseInt(weekData[time]);
    var rep = { date: parsed, deaths: intDeaths };
    arr.push(rep);
  }
  return arr;
}
