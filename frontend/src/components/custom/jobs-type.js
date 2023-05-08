import {Autocomplete} from "@material-ui/lab";
import TextField from "@material-ui/core/TextField";
import React, {useEffect} from "react";
import axios from "axios";

export default function JobType(props) {
    const [value, setValue] = React.useState('');
    const [inputValue, setInputValue] = React.useState('');
    const [jobsTypeList, setJobsTypeList] = React.useState([]);

    function jobType() {
        console.log(process.env)
        axios.get(`${process.env.REACT_APP_BACKEND_APP_URL}/api/v1/jobs/unique-signatures`, {})
            .then(
                response => {
                    setJobsTypeList(response.data)
                    return response.data
                }
            )
            .catch(err => console.warn(err));
    }

    useEffect(() => {
        jobType();
    }, []);
    return (
        <Autocomplete
            freeSolo
            value={value}
            onChange={(event, newValue) => {
                setValue(newValue);
            }}
            inputValue={inputValue}
            onInputChange={(event, newInputValue) => {
                setInputValue(newInputValue);
                props.arg(newInputValue)
            }}
            id="controllable-states-demo"
            options={jobsTypeList}
            renderInput={(params) => <TextField {...params} label="Job type"/>}
        />
    );
}
