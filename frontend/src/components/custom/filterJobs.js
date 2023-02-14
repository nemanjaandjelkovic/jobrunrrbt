import * as React from 'react';
import {useEffect, useRef} from 'react';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import {TextField} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";


export default function FilterJobs(props) {
    const [searchParameter, setParameter] = React.useState('');
    const [searchValue, setValue] = React.useState('');
    const textFieldRef = useRef();

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            props.data(searchParameter, searchValue)
        }, 1000)
        return () => clearTimeout(delayDebounceFn)
    }, [searchValue])

    function parameterChange(event) {
        setParameter(event.target.value);
        props.data(event.target.value, searchValue)
    }

    return (
        <div style={{padding: "20px 0px 0px 20px"}}>
            <Typography variant="h4" component="h4">
                Search jobs
            </Typography>
            <FormControl variant="standard">
                <TextField
                    id="demo-helper-text-misaligned"
                    label="Name"
                    onChange={(e) => setValue(e.target.value)}
                    value={searchValue}
                    style={{width: "500px"}}
                    ref={textFieldRef}
                    helperText={"Please enter " + searchParameter + " name"}
                />

            </FormControl>
            <FormControl variant="standard" style={{width: "200px", marginLeft: "5px"}}>
                <InputLabel id="demo-simple-select-standard-label">Select parameter</InputLabel>
                <Select
                    labelId="demo-simple-select-standard-label"
                    id="demo-simple-select-standard"
                    value={searchParameter}
                    onChange={parameterChange}
                    label="Select parameter"
                >
                    <MenuItem value="All">
                        <em>All</em>
                    </MenuItem>
                    <MenuItem value={"class"}>Class</MenuItem>
                    <MenuItem value={"method"}>Method</MenuItem>
                </Select>
            </FormControl>


        </div>
    );
};