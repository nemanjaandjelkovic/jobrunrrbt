import Box from "@material-ui/core/Box";
import React, {useCallback, useEffect, useState} from "react";
import JobType from "./jobs-type";
import TextField from "@material-ui/core/TextField";
import ActionButton from "./actionButton";
import RemoveIcon from '@material-ui/icons/Remove';
import Grid from '@material-ui/core/Grid';

export default function JobCreate(props) {
    const rowId = props.rowId
    let [argFieldsDisplay, setArgFieldsDisplay] = useState([{argRow: 0, argData: ""}])
    let [jobTypeName, setJobTypeName] = useState()
    let [jobDate, setJobDate] = useState(new Date().toISOString().substring(0, 16))

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            if (jobTypeName !== undefined) {
                props.jobData.jobSignature = jobTypeName
                props.jobData.jobArg = argFieldsDisplay
                props.jobData.jobDate = jobDate
            }
        }, 1000)
        return () => clearTimeout(delayDebounceFn)
    }, [jobTypeName, argFieldsDisplay, jobDate])

    const addArgumentField = useCallback(() => {
        let argId = argFieldsDisplay.length > 0 ? parseInt(argFieldsDisplay[argFieldsDisplay.length - 1].argRow) + 1 : 0
        setArgFieldsDisplay([...argFieldsDisplay, {argRow: argId, argData: ""}])
    }, [argFieldsDisplay]);

    const removeArgumentField = useCallback((argNumber) => {
        const rows = [...argFieldsDisplay];
        rows.splice(argFieldsDisplay.length - argNumber, argNumber);
        setArgFieldsDisplay(rows);
    }, [argFieldsDisplay]);

    const resetArgumentField = useCallback(() => {
        const rows = [{argRow: 0, argData: ""}];
        setArgFieldsDisplay(rows);
    }, []);

    function argumentsDisplay(jobTypeName) {
        let argumentsCount = jobTypeName.split(',').length
        setJobTypeName(jobTypeName)
        if (argFieldsDisplay.length < argumentsCount) {
            addArgumentField();
        } else if (argFieldsDisplay.length > argumentsCount) {
            removeArgumentField(argFieldsDisplay.length - argumentsCount);
        }
        if (!jobTypeName) {
            resetArgumentField();
        }
    }


    return (<Box my={3}>
            <Grid container spacing={1} style={{display: "flex", alignItems: "center"}}>
                <Grid item xs={6}>
                    <JobType arg={argumentsDisplay}></JobType>
                </Grid>
                <Grid item xs style={{display: "flex", flexDirection: "column"}}>
                    {argFieldsDisplay.map(item => {
                        return <TextField id="standard-basic" label="Arguments" variant="standard" fullWidth
                                          key={item.argRow}
                                          onChange={(event) => argFieldsDisplay[item.argRow].argData = event.target.value}/>
                    })}
                </Grid>
                <Grid item xs>
                    <TextField
                        id="datetime-local"
                        label="Scheduled time"
                        type="datetime-local"
                        defaultValue={new Date().toISOString().substring(0, 16)}
                        style={{width: "auto"}}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        onChange={(event) => setJobDate(event.target.value)}
                    />
                    <ActionButton value={rowId} background={"red"}
                                  color={"white"}
                                  Icon={<RemoveIcon/>}
                                  onClick={props.remove}>
                    </ActionButton>

                </Grid>
            </Grid>
        </Box>

    )
}

