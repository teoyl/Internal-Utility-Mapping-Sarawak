/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global utils, echarts */

$(document).ready(function() {

    // Function to fetch data from the API
    function initChart(containerId, jobStatus) {
        const myChart = echarts.init(document.getElementById(containerId));
        
        $.ajax({
            url: 'getDashboardDataRouteJobMain',
            method: 'POST',
            data: {},
            dataType: 'json',
            success: function(response) {
                if (response.status === "Y") {
                    console.log('Success:', response);

                    // Process the fetched data to fit the chart format
                    const responseArray = [response];
                    
                    if(jobStatus === "A") {
                        var chartData = responseArray.flatMap(item => [
                            { value: 0, name: 'Unassigned'},
                            { value: item.appCount, name: 'In Progress'},
                            { value: item.completedAppCount, name: 'Completed'}
                        ]);
                    } else {
                        var chartData = responseArray.flatMap(item => [
                            { value: 0, name: 'Unassigned'},
                            { value: item.jobCount, name: 'In Progress'},
                            { value: item.completedJobCount, name: 'Completed'}
                        ]);
                    }
                
                    // Specify chart configuration item and data
                    var option = {
                        color: ['#dc3912', '#3366cc', '#ff9900'],
                        tooltip: {
                            trigger: 'item'
                        },
                        legend: {
                            orient: 'horizontal',
                            left: 'center',
                            top: 'bottom',
                            formatter: function (name) {
                                const item = chartData.find(data => data.name === name);
                                return name + `: ` + item.value;
                            }
                        },
                        series: [
                            {
                                type: 'pie',
                                radius: '50%',
                                data: chartData,
                                emphasis: {
                                    itemStyle: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ],
                        label: {
                            //formatter: '{b}: {d}%'
                            formatter: '{d}%'
                        }
                    };

                    // Use the specified configuration item and data to show the chart
                    myChart.setOption(option);
                }
            },
            error: function(error) {
                console.error('Error fetching data:', error);
            }
        });
    }

    // Fetch data and initialize the chart
    initChart("appplicationChart", "A");
    initChart("submissionChart", "B");
});


