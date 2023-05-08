import React, {useRef} from "react";
import Accordion from "@material-ui/core/Accordion";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import Typography from "@material-ui/core/Typography";
import ExpandMore from "@material-ui/icons/ExpandMore";
import FormControl from "@material-ui/core/FormControl";
import {TextField} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import {Save} from "@material-ui/icons";
import axios from "axios";


export default function EditJob(props) {
    const packetName = useRef();
    const methodName = useRef();
    const className = useRef();
    const date = useRef();

    let classNameWithFirstCharUpper

    // async function updateJobWithTime(e) {
        function updateJobWithTime(e) {
        classNameWithFirstCharUpper = className.current.value.charAt(0).toUpperCase() + className.current.value.slice(1).toLowerCase()

        axios.put(`${process.env.REACT_APP_BACKEND_APP_URL}/api/v1/jobs/${props.jobInfo.id}`, {
            packageName: packetName.current.value,
            methodName: methodName.current.value,
            className: classNameWithFirstCharUpper,
            scheduledTime: date.current.value + ":00.000000Z"
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
        // const response = await fetch(`${process.env.REACT_APP_BACKEND_APP_URL}/api/v1/jobs/${props.jobInfo.id}`, {
        //     method: "PUT",
        //     headers: {
        //         "Content-Type": "application/json",
        //     },
        //     body: JSON.stringify({
        //         packageName: packetName.current.value,
        //         methodName: methodName.current.value,
        //         className: classNameWithFirstCharUpper,
        //         scheduledTime: date.current.value + ":00.000000Z"
        //     }),
        // });
        // window.location.reload();
    }

    // async function updateJobWithOutTime(e) {
       function updateJobWithOutTime(e) {
        classNameWithFirstCharUpper = className.current.value.charAt(0).toUpperCase() + className.current.value.slice(1).toLowerCase()

           axios.put(`${process.env.REACT_APP_BACKEND_APP_URL}/api/v1/jobs/${props.jobInfo.id}`, {
               packageName: packetName.current.value,
               methodName: methodName.current.value,
               className: classNameWithFirstCharUpper
           }, {
               headers: {
                   'Content-Type': 'application/json'
               }
           })
           props.jobInfo.jobDetails.packageName=packetName.current.value
           props.onJobChange(props.jobInfo)
        // const response = await fetch(`${process.env.REACT_APP_BACKEND_APP_URL}/api/v1/jobs/${props.jobInfo.id}`, {
        //     method: "PUT",
        //     headers: {
        //         "Content-Type": "application/json",
        //     },
        //     body: JSON.stringify({
        //         packageName: packetName.current.value,
        //         methodName: methodName.current.value,
        //         className: classNameWithFirstCharUpper,
        //     }),
        // });
        //window.location.reload();
    }

    let dateJob
    if (props.jobInfo.jobHistory[props.jobInfo.jobHistory.length - 1].scheduledAt !== undefined) {
        let dateFromJob = new Date(props.jobInfo.jobHistory[props.jobInfo.jobHistory.length - 1].scheduledAt);
        let dateJobWithPlusOneHour = dateFromJob.setHours(dateFromJob.getHours() + 1)
        dateJob = new Date(dateJobWithPlusOneHour).toISOString().substring(0, 16);
    } else {
        dateJob = new Date(Date.now()).toISOString().substring(0, 16)
    }

    const jobDisplay = NameDisplay()

    function NameDisplay() {
        const jobInfo = props.jobInfo.jobDetails.className.split(".")
        const classNameDisplay = jobInfo[jobInfo.length - 1]
        jobInfo.pop(classNameDisplay)
        const packetNameDisplay = jobInfo.join('.')
        return [packetNameDisplay, classNameDisplay]
    }

    return (
        <div>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMore/>}
                    aria-controls="panel1a-content"
                    id="edit-job"
                >
                    <Typography>Edit Job</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <FormControl variant="standard">
                        <TextField
                            id="demo-helper-text-misaligned"
                            label="Package name"
                            style={{width: "500px", marginBottom: "10px"}}
                            inputRef={packetName}
                            defaultValue={jobDisplay[0]}

                        />
                        <TextField
                            id="demo-helper-text-misaligned"
                            label="Class name"
                            style={{width: "500px", marginBottom: "10px"}}
                            inputRef={className}
                            defaultValue={jobDisplay[1]}

                        />
                        <TextField
                            id="demo-helper-text-misaligned"
                            label="Method name"
                            style={{width: "500px", marginBottom: "10px"}}
                            inputRef={methodName}
                            defaultValue={props.jobInfo.jobDetails.staticFieldName + "." + props.jobInfo.jobDetails.methodName}
                        />
                        {props.state !== 'SUCCEEDED' && props.state !== 'DELETED' && props.state !== 'FAILED' &&
                            <TextField
                                id="datetime-local"
                                label="Scheduled time"
                                type="datetime-local"
                                defaultValue={dateJob}
                                style={{width: "500px", marginBottom: "10px"}}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                                inputRef={date}
                            />
                        }
                        {
                            props.state === "SCHEDULED" ?
                                <Button variant="contained" endIcon={<Save/>}
                                        onClick={updateJobWithTime}>Update</Button> :
                                <Button variant="contained" endIcon={<Save/>}
                                        onClick={updateJobWithOutTime}>Update</Button>
                        }
                    </FormControl>
                </AccordionDetails>
            </Accordion>

        </div>
    );
}